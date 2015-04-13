import java.util.LinkedList;
import java.util.Random;

public class Andar {
	public static final int SOBE = 1;
	public static final int DESCE = -1;
	public static final int PARADO = 0;

	private int id;
	private LinkedList<Pessoa> pessoas;
	private int statusAndar;
	private int totalPessoas;

	public Andar() {
		this(0,3);
	}

	public Andar(int id, int totalPessoas) {
		this.id = id;
		this.totalPessoas = totalPessoas;
		adicionarPessoas();
	}

	public Andar(int id, LinkedList<Pessoa> pessoas, int statusAndar, int totalPessoas) {
		super();
		this.id = id;
		this.pessoas = pessoas;
		this.statusAndar = Andar.PARADO;
		this.totalPessoas = totalPessoas;
		adicionarPessoas();
	}

	public void adicionarPessoas(){
		Random random = new Random();
		pessoas = new LinkedList<Pessoa>();
		for(int i = 0; i < this.totalPessoas;i++){
			int destino = -1;
			while(destino == -1){
				int aux = random.nextInt(Orquestrador.NUM_ANDARES);
				if(aux != this.id){
					destino = aux;
				}
			}
			((LinkedList<Pessoa>) pessoas).push(new Pessoa(this.id, destino,0));
		}
	}
	
	public void removerPessoas(LinkedList<Pessoa> pessoas){
		for (Pessoa aux : pessoas) {
			Pessoa pessoa = this.pessoas.peek();
			if(pessoa != null && pessoa.getDestino() == aux.getDestino() && pessoa.getOrigem() == aux.getOrigem()){
				this.pessoas.poll();
			}
		}
	}
	
	public LinkedList<Pessoa> getPessoas() {
		LinkedList<Pessoa> aux = new LinkedList<Pessoa>();
		for (Pessoa pessoa : pessoas) {
			try {
				aux.add((Pessoa)pessoa.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return aux;
	}

	@Override
	public String toString() {
//		String aux = String.format("\tId: %d Status: %d Pessoas [", id, statusAndar);
		String aux = String.format("\tId: %d Pessoas [", id);
		for (Pessoa p : pessoas) {
			aux += p.toString() + ", ";
		}
		return aux + "]\n";
	}

}
