# ============================================================
# setup-github-api.ps1
# Cria labels, milestones e issues no GitHub usando a API REST.
#
# Pre-requisitos:
#   - Personal Access Token com escopo repo
#   - GitHub token disponível via parâmetro -Token ou variável de ambiente GITHUB_TOKEN
#
# Uso:
#   .\setup-github-api.ps1 -Repo "seu-usuario/seu-repositorio" -Token "SEU_TOKEN"
#   $env:GITHUB_TOKEN = "SEU_TOKEN"; .\setup-github-api.ps1 -Repo "seu-usuario/seu-repositorio"
#
# Exemplo:
#   .\setup-github-api.ps1 -Repo "ProfBezerra/gabarito-crud-grasp-produto"
# ============================================================

param(
    [Parameter(Mandatory = $true)]
    [string]$Repo,

    [Parameter(Mandatory = $false)]
    [string]$Token
)

$ErrorActionPreference = "Stop"

if (-not $Token) {
    $Token = $env:GITHUB_TOKEN
}

if (-not $Token) {
    Write-Error "Token do GitHub é obrigatório. Use -Token ou defina a variável de ambiente GITHUB_TOKEN."
    exit 1
}

$ScriptDir   = Split-Path -Parent $MyInvocation.MyCommand.Path
$ConfigDir   = Join-Path $ScriptDir "config"
$LabelsFile  = Join-Path $ConfigDir "labels.json"
$MilesFile   = Join-Path $ConfigDir "milestones.json"
$BacklogFile = Join-Path $ConfigDir "backlog.json"
$ApiBase     = "https://api.github.com"

function Invoke-GitHubApi {
    param(
        [Parameter(Mandatory = $true)][string]$Method,
        [Parameter(Mandatory = $true)][string]$Endpoint,
        [Parameter(Mandatory = $false)]$Body
    )

    $headers = @{
        Authorization = "token $Token"
        Accept        = "application/vnd.github+json"
        "User-Agent" = "setup-github-api-script"
    }

    $params = @{
        Uri         = "$ApiBase/$Endpoint"
        Method      = $Method
        Headers     = $headers
        ErrorAction = 'Continue'
    }

    if ($Body -ne $null) {
        $params.ContentType = 'application/json'
        $params.Body        = $Body | ConvertTo-Json -Compress
    }

    try {
        return Invoke-RestMethod @params
    } catch {
        $response = $_.ErrorDetails.Message
        if ($response) {
            throw "API Error: $response"
        } else {
            throw $_
        }
    }
}

function Show-Result {
    param([string]$Label, [bool]$Success)
    if ($Success) {
        Write-Host "  [OK] $Label" -ForegroundColor Green
    } else {
        Write-Host "  [ERRO] $Label" -ForegroundColor Red
    }
}

function Get-ExistingIssues {
    $issues = @()
    $endpoint = "repos/$Repo/issues?state=all&per_page=100"

    while ($endpoint) {
        $response = Invoke-WebRequest -Uri "$ApiBase/$endpoint" -Headers @{
            Authorization = "token $Token"
            Accept        = "application/vnd.github+json"
            "User-Agent" = "setup-github-api-script"
        } -ErrorAction Stop

        $pageIssues = $response.Content | ConvertFrom-Json
        if ($pageIssues -is [array]) {
            $issues += $pageIssues
        } else {
            $issues += @($pageIssues)
        }
        
        $linkHeader = $response.Headers.'Link'

        if ($linkHeader -and $linkHeader -match '<([^>]+)>; rel="next"') {
            $endpoint = $matches[1] -replace 'https://api.github.com/', ''
        } else {
            $endpoint = $null
        }
    }

    return $issues
}

Write-Host "`n=== Verificando arquivos de configuração ===" -ForegroundColor Cyan
Get-Item $LabelsFile -ErrorAction Stop | Out-Null
Get-Item $MilesFile -ErrorAction Stop | Out-Null
Get-Item $BacklogFile -ErrorAction Stop | Out-Null
Write-Host "Arquivos de configuração encontrados." -ForegroundColor Green
Write-Host "Repositorio alvo: $Repo" -ForegroundColor Green

# ============================================================
# 1. LABELS
# ============================================================
Write-Host "`n=== Criando labels ===" -ForegroundColor Cyan
$labels = Get-Content $LabelsFile -Raw | ConvertFrom-Json

foreach ($label in $labels) {
    try {
        $body = [pscustomobject]@{
            name        = $label.name
            color       = $label.color
            description = $label.description
        }

        Invoke-GitHubApi -Method Post -Endpoint "repos/$Repo/labels" -Body $body
        Show-Result "Label: $($label.name)" $true
    } catch {
        if ($_.Exception.Response -and $_.Exception.Response.StatusCode.Value__ -eq 422) {
            try {
                $escapedName = [uri]::EscapeDataString($label.name)
                Invoke-GitHubApi -Method Patch -Endpoint "repos/$Repo/labels/$escapedName" -Body $body
                Show-Result "Label existente atualizado: $($label.name)" $true
            } catch {
                $errorMsg = $_.Exception.Message
                Show-Result "Label: $($label.name) - $errorMsg" $false
            }
        } else {
            $errorMsg = $_.Exception.Message
            Show-Result "Label: $($label.name) - $errorMsg" $false
        }
    }
}

# ============================================================
# 2. MILESTONES
# ============================================================
Write-Host "`n=== Criando milestones (sprints) ===" -ForegroundColor Cyan
$milestones = Get-Content $MilesFile -Raw | ConvertFrom-Json
$milestoneMap = @{}

foreach ($ms in $milestones) {
    $body = [pscustomobject]@{
        title       = $ms.title
        description = $ms.description
        due_on      = $ms.due_on
        state       = "open"
    }

    try {
        $created = Invoke-GitHubApi -Method Post -Endpoint "repos/$Repo/milestones" -Body $body
        $msNum = $created.number
        if ($msNum -is [array]) {
            $msNum = $msNum[0]
        }
        $milestoneMap[$ms.title] = $msNum
        Show-Result "Milestone: $($ms.title) (#$msNum)" $true
    } catch {
        try {
            $allMilestones = Invoke-GitHubApi -Method Get -Endpoint "repos/$Repo/milestones?state=all&per_page=100"
            $existing = $allMilestones | Where-Object { $_.title -eq $ms.title } | Select-Object -First 1
            if ($existing) {
                $msNumber = $existing.number
                if ($msNumber -is [array]) {
                    $msNumber = $msNumber[0]
                }
                $milestoneMap[$ms.title] = $msNumber
                Show-Result "Milestone ja existe: $($ms.title) (#$msNumber)" $true
            } else {
                $errorMsg = $_.Exception.Message
                Show-Result "Milestone: $($ms.title) - $errorMsg" $false
            }
        } catch {
            $errorMsg = $_.Exception.Message
            Show-Result "Milestone: $($ms.title) - $errorMsg" $false
        }
    }
}

# ============================================================
# 3. ISSUES
# ============================================================
Write-Host "`n=== Criando issues do backlog ===" -ForegroundColor Cyan
$backlog = Get-Content $BacklogFile -Raw | ConvertFrom-Json
$existingIssues = Get-ExistingIssues

foreach ($sprint in $backlog) {
    $msNumber = $milestoneMap[$sprint.milestone]
    if ($msNumber -is [array]) {
        $msNumber = $msNumber[0]
    }
    Write-Host "`n  >> $($sprint.milestone)" -ForegroundColor Yellow

    foreach ($issue in $sprint.issues) {
        $exists = $existingIssues | Where-Object { $_.title -eq $issue.title } | Select-Object -First 1
        if ($exists) {
            Show-Result "Issue já existe: $($issue.title)" $true
            continue
        }

        $bodyHash = @{
            title     = $issue.title
            body      = $issue.body
            labels    = $issue.labels
        }

        if ($msNumber) {
            $bodyHash['milestone'] = [int]$msNumber
        }

        $body = [pscustomobject]$bodyHash

        try {
            Invoke-GitHubApi -Method Post -Endpoint "repos/$Repo/issues" -Body $body
            Show-Result $issue.title $true
        } catch {
            $errorMsg = $_.Exception.Message
            $jsonPayload = $body | ConvertTo-Json -Compress
            Write-Host "    Payload: $jsonPayload" -ForegroundColor DarkGray
            Show-Result "$($issue.title) - $errorMsg" $false
        }
    }
}

# ============================================================
# 4. PROJECT (Kanban) - opcional
# ============================================================
Write-Host "`n=== Tentando criar GitHub Project (Kanban) ===" -ForegroundColor Cyan
$projectName = "Migracao para Web - Spring Boot e Angular"
$projectBody = [pscustomobject]@{
    name = $projectName
    body = "Quadro de acompanhamento do projeto gerado via API"
}

try {
    $projectResponse = Invoke-GitHubApi -Method Post -Endpoint "repos/$Repo/projects" -Body $projectBody
    Write-Host "  [OK] Projeto criado: $projectName" -ForegroundColor Green
    Write-Host "  Acesse: $($projectResponse.html_url)" -ForegroundColor DarkGray
} catch {
    Write-Host "  [AVISO] Não foi possível criar o projeto automaticamente via API." -ForegroundColor Yellow
    Write-Host "  Crie manualmente em: https://github.com/$Repo/projects" -ForegroundColor DarkGray
}

# ============================================================
# Resumo final
# ============================================================
Write-Host "`n=== Configuracao concluida! ===" -ForegroundColor Cyan
Write-Host "Acesse seu repositorio para ver o resultado:" -ForegroundColor White
Write-Host "  https://github.com/$Repo/issues" -ForegroundColor DarkCyan
Write-Host "  https://github.com/$Repo/milestones" -ForegroundColor DarkCyan
Write-Host "  https://github.com/$Repo/projects" -ForegroundColor DarkCyan
