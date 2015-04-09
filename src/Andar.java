import java.util.ArrayList;

public class Andar {
	private int id;
	private ArrayList<Pessoa> pessoas;
	private int statusAndar;
	public static final int SOBE = 1;
	public static final int DESCE = -1;
	public static final int PARADO = 0;

	public Andar() {
		this.id = 0;
		gerarPessoas();
	}

	public Andar(int id) {
		this.id = id;
		gerarPessoas();
	}

	public Andar(int id, ArrayList<Pessoa> pessoas, int statusAndar) {
		super();
		this.id = id;
		this.pessoas = pessoas;
		this.statusAndar = Andar.PARADO;
	}

	public void gerarPessoas(){
		pessoas = new ArrayList<Pessoa>(3);
		pessoas.add(new Pessoa());
		pessoas.add(new Pessoa());
		pessoas.add(new Pessoa());
	}
	
	@Override
	public String toString() {
		String aux = String.format("\tId: %d Status: %d Pessoas [", id, statusAndar);
		for (Pessoa p : pessoas) {
			aux += p.toString() + ", ";
		}
		return aux + "]\n";
	}

}
