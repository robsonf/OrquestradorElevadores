import java.util.ArrayList;


public class Elevador {
	public static final int SOBE = 1;
	public static final int DESCE = -1;
	public static final int PARADO = 0;
	
	private int id;
	private int andarAtual;
	private ArrayList<Integer> andarDestino;
	private int status;
	private int andaresPercorridos;
	
	public Elevador(int id) {
		this(id, 0, null, Elevador.PARADO, 0);
		andarDestino = new ArrayList<Integer>(2);
		adicionarDestino(0);
		adicionarDestino(1);
	}

	public Elevador(int id, int andarAtual, ArrayList<Integer> andarDestino, int status, int andaresPercorridos) {
		super();
		this.id = id;
		this.andarAtual = andarAtual;
		this.andarDestino = andarDestino;
		this.status = status;
		this.andaresPercorridos = andaresPercorridos;
	}
	
	public void adicionarDestino(int id) {
		andarDestino.add(id);
	}
	
	@Override
	public String toString() {
		String aux = String.format("\tId: %d Status: %d AndarAtual: %d AndarDestino: %d AndaresPercorridos: %d", id, status, andarAtual, andaresPercorridos);
		for (int d : andarDestino) {
			aux += d + ", ";
		}
		return aux;
	}
}
