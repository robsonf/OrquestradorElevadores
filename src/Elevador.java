import java.util.ArrayList;


public class Elevador {
	public static final int SOBE = 1;
	public static final int DESCE = -1;
	public static final int PARADO = 0;
	public static final int CAPACIDADE_MAX = 0;
	
	private int id;
	private int andarAtual;
	private boolean [] destinos;
	private int status;
	private int andaresPercorridos;
	private ArrayList<Pessoa> pessoas;
	
	public Elevador(int id) {
		this(id, 0, null, Elevador.PARADO, 0);
		this.destinos = new boolean [Orquestrador.NUM_ANDARES];
	}

	public Elevador(int id, int andarAtual, boolean [] destinos, int status, int andaresPercorridos) {
		super();
		this.id = id;
		this.andarAtual = andarAtual;
		this.destinos = destinos;
		this.status = status;
		this.andaresPercorridos = andaresPercorridos;
	}
	
	public void adicionarDestino(int id) {
		if(!this.destinos[id])
			this.destinos[id] = true;
	}

	@Override
	public String toString() {
		String aux = String.format("\tId: %d Status: %d AndarAtual: %d AndaresPercorridos: %d\nAndarDestino:", id, status, andarAtual, andaresPercorridos);
		for (int i = 0; i < destinos.length; i++) {
			if(this.destinos[i])
				aux += i + ", ";
		}
		return aux;
	}
}
