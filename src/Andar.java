import java.util.ArrayList;
import java.util.Random;

public class Andar {
	public static final int SOBE = 1;
	public static final int DESCE = -1;
	public static final int PARADO = 0;

	private int id;
	private ArrayList<Pessoa> pessoas;
	private int statusAndar;
	private int totalPessoas;
	private boolean [] destinos;

	public Andar() {
		this(0,3);
	}

	public Andar(int id, int totalPessoas) {
		this.id = id;
		this.totalPessoas = totalPessoas;
		this.destinos = new boolean [Orquestrador.NUM_ANDARES];
		gerarPessoas();
	}

	public Andar(int id, ArrayList<Pessoa> pessoas, int statusAndar, int totalPessoas) {
		super();
		this.id = id;
		this.pessoas = pessoas;
		this.statusAndar = Andar.PARADO;
		this.totalPessoas = totalPessoas;
		this.destinos = new boolean [Orquestrador.NUM_ANDARES];
		gerarPessoas();
	}

	public void gerarPessoas(){
		Random random = new Random();
		pessoas = new ArrayList<Pessoa>();
		for(int i = 0; i < this.totalPessoas;i++){
			int destino = -1;
			while(destino == -1){
				int aux = random.nextInt(Orquestrador.NUM_ANDARES);
				if(aux != this.id){
					destino = aux;
					if(!this.destinos[destino])
						this.destinos[destino] = true;
				}
			}
			pessoas.add(new Pessoa(this.id, destino,0));
		}
	}

	public boolean [] getDestinos(){
		return destinos;
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
