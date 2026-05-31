import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { BarracaService } from '../../services/barraca.service';
import { TipoBarracaService } from '../../services/tipo-barraca.service';
import { TipoBarracaDTO } from '../../models/tipo-barraca.model';

@Component({
  selector: 'app-barraca-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './barraca-form.component.html',
  styleUrl: './barraca-form.component.css'
})
export class BarracaFormComponent implements OnInit {
  form!: FormGroup;
  tiposBarraca: TipoBarracaDTO[] = [];
  modoEdicao = false;
  idEdicao?: number;
  mensagemErro = '';

  private fb = inject(FormBuilder);
  private barracaService = inject(BarracaService);
  private tipoService = inject(TipoBarracaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  ngOnInit(): void {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
      descricao: [''],
      localizacao: [''],
      ativo: [true],
      tipoBarracaId: ['', Validators.required]
    });

    this.tipoService.listar().subscribe({
      next: (dados) => this.tiposBarraca = dados,
      error: () => this.mensagemErro = 'Não foi possível carregar os tipos de barraca.'
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.modoEdicao = true;
      this.idEdicao = +id;
      this.barracaService.buscarPorId(this.idEdicao).subscribe({
        next: (dados) => {
          this.form.patchValue({
            nome: dados.nome,
            descricao: dados.descricao,
            localizacao: dados.localizacao,
            ativo: dados.ativo,
            tipoBarracaId: dados.tipoBarracaId
          });
        },
        error: () => this.mensagemErro = 'Não foi possível carregar os dados da barraca.'
      });
    }
  }

  salvar(): void {
    if (this.form.invalid) return;

    const dados = this.form.value;

    if (this.modoEdicao && this.idEdicao) {
      this.barracaService.atualizar(this.idEdicao, dados).subscribe({
        next: () => this.router.navigate(['/barracas']),
        error: (err) => this.mensagemErro = err.error?.mensagem || 'Erro ao atualizar a barraca.'
      });
    } else {
      this.barracaService.cadastrar(dados).subscribe({
        next: () => this.router.navigate(['/barracas']),
        error: (err) => this.mensagemErro = err.error?.mensagem || 'Erro ao cadastrar a barraca.'
      });
    }
  }

  get nome() { return this.form.get('nome'); }
  get tipoBarracaId() { return this.form.get('tipoBarracaId'); }
}
