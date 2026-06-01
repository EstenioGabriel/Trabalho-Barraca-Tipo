import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
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
  private cdr = inject(ChangeDetectorRef);

  ngOnInit(): void {
    this.carregarBarracas();
  }

  carregarBarracas(): void {
    this.barracaService.listar().subscribe({
      next: (dados) => {
        this.barracas = dados;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.mensagemErro = 'Não foi possível carregar a lista de barracas.';
        this.cdr.markForCheck();
      }
    });
  }

  deletar(id: number): void {
    if (confirm('Tem certeza de que deseja remover esta barraca?')) {
      this.barracaService.remover(id).subscribe({
        next: () => {
          this.mensagemSucesso = 'Barraca removida com sucesso!';
          this.carregarBarracas();
          this.cdr.markForCheck();
          setTimeout(() => {
            this.mensagemSucesso = '';
            this.cdr.markForCheck();
          }, 3000);
        },
        error: (err) => {
          this.mensagemErro = err.error?.message || 'Falha ao excluir a barraca.';
          this.cdr.markForCheck();
          setTimeout(() => {
            this.mensagemErro = '';
            this.cdr.markForCheck();
          }, 4000);
        }
      });
    }
  }
}
