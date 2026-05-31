export interface BarracaRequestDTO {
  nome: string;
  descricao?: string;
  localizacao?: string;
  ativo: boolean;
  tipoBarracaId: number;
}
