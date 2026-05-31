import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TipoBarracaDTO } from '../models/tipo-barraca.model';

@Injectable({
  providedIn: 'root'
})
export class TipoBarracaService {
  private apiUrl = 'http://localhost:8080/api/tipos-barraca';
  private http = inject(HttpClient);

  listar(): Observable<TipoBarracaDTO[]> {
    return this.http.get<TipoBarracaDTO[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<TipoBarracaDTO> {
    return this.http.get<TipoBarracaDTO>(`${this.apiUrl}/${id}`);
  }

  cadastrar(tipoBarraca: TipoBarracaDTO): Observable<TipoBarracaDTO> {
    return this.http.post<TipoBarracaDTO>(this.apiUrl, tipoBarraca);
  }

  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
