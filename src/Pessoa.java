

public class Pessoa {
	private int origem;
	private int destino;
	private int tempo;
	private int andarAtual;

	public Pessoa() {
		this.origem = 0;
		this.destino = 1;
		this.tempo = 0;
		this.andarAtual = 0;
	}

	public Pessoa(int origem, int destino, int tempo) {
		super();
		this.origem = origem;
		this.destino = destino;
		this.tempo = tempo;
		this.andarAtual = origem;
	}
	
	public void incrementaTempo(){
		this.tempo++;
	}
	
	public int getDestino() {
		return destino;
	}

	
	public int getOrigem() {
		return origem;
	}

	public int getTempo() {
		return tempo;
	}
	public int getAndarAtual() {
		return andarAtual;
	}

	public void setAndarAtual(int andarAtual) {
		this.andarAtual = andarAtual;
	}
	@Override
	public String toString() {
		return String.format("(O:%d,D:%d,T:%d)", origem, destino, tempo);
	}
	
	@Override
	protected Pessoa clone() throws CloneNotSupportedException {
		return new Pessoa(this.origem, this.destino, this.tempo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + destino;
		result = prime * result + origem;
		result = prime * result + tempo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (destino != other.destino)
			return false;
		if (origem != other.origem)
			return false;
		if (tempo != other.tempo)
			return false;
		return true;
	}
	
	/*
	 * retorna valor positivo se subir e negativo se descer
	 */
	public int getDirecao(){
		int direcao = this.destino - this.origem;
		if(direcao > 0)
			return Elevador.SUBIR;
		else
			return Elevador.DESCER;
	}
}
