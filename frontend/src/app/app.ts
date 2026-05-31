import { Component, OnInit, signal, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TipoBarracaService } from './services/tipo-barraca.service';
import { BarracaService } from './services/barraca.service';
import { BarracaListComponent } from './components/barraca-list/barraca-list.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, BarracaListComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  protected readonly title = signal('frontend');

  private tipoBarracaService = inject(TipoBarracaService);
  private barracaService = inject(BarracaService);

  ngOnInit(): void {
    this.tipoBarracaService.listar().subscribe({
      next: (tipos) => console.log('Tipos de Barraca carregados do backend:', tipos),
      error: (err) => console.error('Erro ao carregar tipos de barraca:', err)
    });

    this.barracaService.listar().subscribe({
      next: (barracas) => console.log('Barracas carregadas do backend:', barracas),
      error: (err) => console.error('Erro ao carregar barracas:', err)
    });
  }
}
