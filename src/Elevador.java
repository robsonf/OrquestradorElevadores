import java.util.LinkedList;


public class Elevador {
	public static final int SOBE = 1;
	public static final int DESCE = -1;
	public static final int PARADO = 0;
	public static final int CAPACIDADE_MAX = 3;
	
	private int id;
	private int andarAtual;
	private boolean [] destinos;
	private int status;
	private int andaresPercorridos;
	private LinkedList<Pessoa> pessoas;
	
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
		this.pessoas = new LinkedList<Pessoa>();
	}
	public void adicionarPessoa(Pessoa pessoa){
		try {
			pessoas.add(pessoa.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		atualizarDestinos();
	}
	public void removerPessoas(LinkedList<Pessoa> pessoas){
		for (Pessoa aux : pessoas) {
			if(aux.getDestino() == this.andarAtual)
				this.pessoas.remove(aux);
		}
		atualizarDestinos();
	}
	
	public boolean estaLotado(){
		if(pessoas.size()==CAPACIDADE_MAX)
			return true;
		else
			return false;
	}
	
	public void atualizarDestinos() {
		this.destinos = new boolean [Orquestrador.NUM_ANDARES];
		for (Pessoa pessoa : pessoas) {
			if(!this.destinos[pessoa.getDestino()])
				this.destinos[pessoa.getDestino()] = true;
		}
	}
	public void atualizarStatus(int status){
		if(status == SOBE && andarAtual < Orquestrador.NUM_ANDARES){
			this.andarAtual++;
			this.andaresPercorridos++;
		}else if(status == DESCE && andarAtual > 0){
			this.andarAtual--;
			this.andaresPercorridos++;
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
	
	public int getAndarAtual() {
		return andarAtual;
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
