export interface BarracaResponseDTO {
  id: number;
  nome: string;
  descricao?: string;
  localizacao?: string;
  ativo: boolean;
  tipoBarracaId: number;
  tipoBarracaNome: string;
}
