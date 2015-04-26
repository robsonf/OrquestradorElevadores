import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Andar {
	public static final int SOBE = 1;
	public static final int DESCE = -1;
	public static final int PARADO = 0;
	// aumenta o peso do tempo de espera para as pessoas que não foram atendidas
	public static final int PESO_ESPERA = 2;

	private int id;
	private Queue<Pessoa> pessoas;
	private int statusAndar;
	private int totalPessoas;

	public Andar() {
		this(0,3);
	}

	public Andar(int id, int totalPessoas) {
		this.id = id;
		this.totalPessoas = totalPessoas;
		iniciarPessoas();
	}

	public Andar(int id, Queue<Pessoa> pessoas, int statusAndar, int totalPessoas) {
		super();
		this.id = id;
		this.pessoas = pessoas;
		this.statusAndar = Andar.PARADO;
		this.totalPessoas = totalPessoas;
		iniciarPessoas();
	}

	public void iniciarPessoas(){
		pessoas = new LinkedList<Pessoa>();
		for(int i = 0; i < this.totalPessoas;i++){
			adicionarPessoa();
		}
	}

	public void adicionarPessoa(){
		Random random = new Random();
		int destino = -1;
//		while(destino == -1){
		while(destino == -1 || destino == Orquestrador.NUM_ANDARES - 1){
			int aux = random.nextInt(Orquestrador.NUM_ANDARES);
			if(aux != this.id){
				destino = aux;
			}
		}
		if(this.id == Orquestrador.NUM_ANDARES-1)
			return;
		pessoas.add(new Pessoa(this.id, destino,Orquestrador.contadorTempo));
	}

	public int tempoPessoasEsperando(){
		int total = 0;
		for (Pessoa pessoa : pessoas) {
			total += (Math.abs(pessoa.getDestino() - pessoa.getOrigem()) * PESO_ESPERA) + (Orquestrador.contadorTempo - pessoa.getTempo());
		}
		return total;
	}
	
	public void removerPessoas(Queue<Pessoa> pessoas){
		for (Pessoa aux : pessoas) {
			Pessoa pessoa = this.pessoas.peek();
			if(pessoa != null && pessoa.getDestino() == aux.getDestino() && pessoa.getOrigem() == aux.getOrigem()){
				this.pessoas.poll();
			}
		}
	}
	
	public boolean estaVazio(){
		return this.pessoas.size() == 0;
	}
	
	public Queue<Pessoa> getPessoas() {
		return this.pessoas;
	}
	
	public int getId(){
		return this.id;
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
