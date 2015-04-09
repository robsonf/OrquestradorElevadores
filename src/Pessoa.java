

public class Pessoa {
	private int origem;
	private int destino;
	private int tempo;

	public Pessoa() {
		this.origem = 0;
		this.destino = 1;
		this.tempo = 0;
	}

	public Pessoa(int origem, int destino, int tempo) {
		super();
		this.origem = origem;
		this.destino = destino;
		this.tempo = tempo;
	}
	
	public void incrementaTempo(){
		this.tempo++;
	}
	
	@Override
	public String toString() {
		return String.format("Origem: %d Destino: %d Tempo: %d", origem, destino, tempo);
	}
}
