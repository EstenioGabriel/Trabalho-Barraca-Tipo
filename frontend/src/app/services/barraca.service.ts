import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BarracaRequestDTO } from '../models/barraca-request.model';
import { BarracaResponseDTO } from '../models/barraca-response.model';

@Injectable({
  providedIn: 'root'
})
export class BarracaService {
  private apiUrl = 'http://localhost:8080/api/barracas';
  private http = inject(HttpClient);

  listar(): Observable<BarracaResponseDTO[]> {
    return this.http.get<BarracaResponseDTO[]>(this.apiUrl);
  }

  buscarPorId(id: number): Observable<BarracaResponseDTO> {
    return this.http.get<BarracaResponseDTO>(`${this.apiUrl}/${id}`);
  }

  cadastrar(barraca: BarracaRequestDTO): Observable<BarracaResponseDTO> {
    return this.http.post<BarracaResponseDTO>(this.apiUrl, barraca);
  }

  atualizar(id: number, barraca: BarracaRequestDTO): Observable<BarracaResponseDTO> {
    return this.http.put<BarracaResponseDTO>(`${this.apiUrl}/${id}`, barraca);
  }

  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
