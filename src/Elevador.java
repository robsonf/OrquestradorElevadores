import java.util.Iterator;
import java.util.LinkedList;


public class Elevador {
	public static final int SUBIR = 1;
	public static final int DESCER = -1;
	public static final int PARAR = 0;
	public static final int CAPACIDADE_MAX = 3;
	
	private int id;
	private int andarAtual;
	private boolean [] destinos;
	private int status;
	private int andaresPercorridos;
	private LinkedList<Pessoa> pessoas;
	
	public Elevador(int id) {
		this(id, 0, null, Elevador.PARAR, 0);
		this.destinos = new boolean [Orquestrador.NUM_ANDARES];
		this.status = SUBIR;
	}
	public Elevador(int id, int andarAtual, boolean [] destinos, int status, int andaresPercorridos) {
		super();
		this.id = id;
		this.andarAtual = andarAtual;
		this.destinos = destinos;
		this.status = status;
		this.andaresPercorridos = andaresPercorridos;
		this.pessoas = new LinkedList<Pessoa>();
	}
	public void adicionarPessoa(Pessoa pessoa){
		pessoas.add(pessoa);
		atualizarDestinos();
	}
	public void removerPessoas(LinkedList<Pessoa> pessoas){
		for (Pessoa aux : pessoas) {
			if(aux.getDestino() == this.andarAtual)
				this.pessoas.remove(aux);
		}
		atualizarDestinos();
	}

	public int removerPessoas(){
		int total = 0;
		Iterator<Pessoa> pessoas = this.pessoas.iterator();
		while (pessoas.hasNext()) {
			if(pessoas.next().getDestino() == this.andarAtual){
				pessoas.remove();
				total++;
			}
		}
		atualizarDestinos();
		return total;
	}
	
	public boolean estaLotado(){
		return pessoas.size() == CAPACIDADE_MAX;
	}
	
	public void atualizarDestinos() {
		this.destinos = new boolean [Orquestrador.NUM_ANDARES];
		for (Pessoa pessoa : pessoas) {
			if(!this.destinos[pessoa.getDestino()])
				this.destinos[pessoa.getDestino()] = true;
		}
	}
	public void atualizarAndar(int acao){
		if(acao == SUBIR){
			if (andarAtual < Orquestrador.NUM_ANDARES - 1){
				this.andarAtual++;
				this.andaresPercorridos++;
			}else if (andarAtual == Orquestrador.NUM_ANDARES - 1){
				System.out.println("............ULTIMO ANDAR..........");
				this.status = DESCER;
				this.andarAtual--;
			}
		}
		if(acao == DESCER){
			if (andarAtual > 0){
				this.andarAtual--;
				this.andaresPercorridos++;
			}else if(andarAtual == 0){
				System.out.println("............PRIMEIRO ANDAR..........");
				this.status = SUBIR;
				this.andarAtual++;
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

	public int getStatus(){
		return this.status;
	}
	public int getAndarAtual() {
		return this.andarAtual;
	}
	
	public boolean estaVazio(){
		return this.pessoas.size() == 0;
	}
	@Override
	public String toString() {
		String aux = String.format("\tId: %d Status: %d AndarAtual: %d AndaresPercorridos: %d\nAndaresDestino:[", id, status, andarAtual, andaresPercorridos);
		for (int i = 0; i < destinos.length; i++) {
			if(this.destinos[i])
				aux += i + ", ";
		}
		aux += String.format("] Pessoas [");
		for (Pessoa p : pessoas) {
			aux += p.toString() + ", ";
		}
		return aux + "]\n";
	}
}
