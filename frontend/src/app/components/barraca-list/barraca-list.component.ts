import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BarracaService } from '../../services/barraca.service';
import { BarracaResponseDTO } from '../../models/barraca-response.model';

@Component({
  selector: 'app-barraca-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './barraca-list.component.html',
  styleUrl: './barraca-list.component.css'
})
export class BarracaListComponent implements OnInit {
  barracas: BarracaResponseDTO[] = [];
  mensagemSucesso = '';
  mensagemErro = '';

  private barracaService = inject(BarracaService);

  ngOnInit(): void {
    this.carregarBarracas();
  }

  carregarBarracas(): void {
    this.barracaService.listar().subscribe({
      next: (dados) => this.barracas = dados,
      error: (err) => this.mensagemErro = 'Não foi possível carregar a lista de barracas.'
    });
  }

  deletar(id: number): void {
    if (confirm('Tem certeza de que deseja remover esta barraca?')) {
      this.barracaService.remover(id).subscribe({
        next: () => {
          this.mensagemSucesso = 'Barraca removida com sucesso!';
          this.carregarBarracas();
          setTimeout(() => this.mensagemSucesso = '', 3000);
        },
        error: (err) => {
          this.mensagemErro = err.error?.message || 'Falha ao excluir a barraca.';
          setTimeout(() => this.mensagemErro = '', 4000);
        }
      });
    }
  }
}
