import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;


public class Elevador {
	public static final int SUBIR = 1;
	public static final int DESCER = -1;
	public static final int PARAR = 0;
	public static final int CAPACIDADE_MAX = 5;
	
	private int id;
	private int andarAtual;
	private boolean [] destinos;
	private LinkedHashSet<Integer> listaChamadas;
	private LinkedHashSet<Integer> listaDestinos;
	private int status;
	private int acao;
	private int andaresPercorridos;
	private Queue<Pessoa> pessoas;
	private ArrayList<Integer> temposEsperaAtendidas;
	private int teto;
	private int chao;
	private ArrayList<Andar> andares;
	private Queue<Pessoa> pessoasAtendidas;
	private Orquestrador orquestrador;
	public LinkedHashSet<Integer> atendeChamadas;
	
	public Elevador(int id) {
		this(id, 0, Elevador.PARAR, Elevador.PARAR, 0, 0, Orquestrador.NUM_ANDARES-1);
	}
	public Elevador(int id, int chao, int teto) {
		this(id, chao, Elevador.PARAR, Elevador.PARAR, 0, chao, teto);
	}
	public Elevador(int id, int andarAtual, int status, int acao, int andaresPercorridos, int chao, int teto) {
		super();
		this.id = id;
		this.andarAtual = andarAtual;
		this.status = status;
		this.acao = acao;
		this.andaresPercorridos = andaresPercorridos;
		this.pessoas = new LinkedList<Pessoa>();
		this.pessoasAtendidas = new LinkedList<Pessoa>();
		this.temposEsperaAtendidas = new ArrayList<Integer>();
		this.chao = chao;
		this.teto = teto;
		this.listaDestinos = new LinkedHashSet<Integer>();
		this.listaChamadas = new LinkedHashSet<Integer>();
		this.atendeChamadas = new LinkedHashSet<Integer> ();
		this.destinos = new boolean [Orquestrador.NUM_ANDARES];
	}
	public void adicionarPessoa(Pessoa pessoa){
		pessoas.add(pessoa);
		atualizarDestinos();
	}
	public int removerPessoas(){
		int total = 0;
		Iterator<Pessoa> pessoas = this.pessoas.iterator();
		while (pessoas.hasNext()) {
			Pessoa pessoa = pessoas.next(); 
			if(pessoa.getDestino() == this.andarAtual){
				int espera = Math.abs(Orquestrador.contadorTempo - pessoa.getTempo());
				try {
					pessoasAtendidas.add(pessoa.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				pessoas.remove();
				total++;
				temposEsperaAtendidas.add(espera);
			}
		}
		atualizarDestinos();
		return total;
	}

	public void atualizarDestinos() {
		this.destinos = new boolean [Orquestrador.NUM_ANDARES];
		this.listaDestinos = new LinkedHashSet<Integer>();
		int maior = teto , menor = chao;
		for (Pessoa pessoa : pessoas) {
			int destino = pessoa.getDestino();
			if(!this.destinos[destino])
				this.destinos[destino] = true;
			if(destino > maior)
				maior = destino;
			if(destino < menor)
				menor = destino;
			this.listaDestinos.add(destino);
		}
		teto = maior;
		chao = menor;
	}

	public void adicionarChamada(int novo) {
		this.listaChamadas.add(novo);
		int maior = teto , menor = chao;
		for (int i = 0; i < destinos.length; i++) {
			if(!this.destinos[novo]){
				this.destinos[novo] = true;
			}else{
				if(i>maior)
					teto = i;
				if(i<menor)
					chao = i;
			}
		}
	}

	private Pessoa quemEntra(Queue<Pessoa> pessoasAndar) {
		for (Pessoa pessoa : pessoasAndar) {
			if(pessoa.getDirecao() == this.getStatus()){
				return pessoa;
			}
		}
		return null;
	}

	/*
	 * se parado, remove pessoas que chegaram ao destino; se nao lotado, 
	 * adiciona pessoas que desejam seguir na mesma direção já programada;
	 * se em movimento, continua acao do movimento programado
	 */
	public void executarAcao(){
		if(this.status!=PARAR){
			if(this.acao==PARAR){
				removerPessoas();
				if(this.andarAtual >= Orquestrador.NUM_ANDARES){
					System.out.println("Erro");
				}
				Andar andar = this.andares.get(this.andarAtual);
				Queue<Pessoa> pessoas = andar.getPessoas();
				Pessoa pessoa = quemEntra(pessoas);
				while(!estaLotado() && pessoa != null){
					try {
						adicionarPessoa(pessoa.clone());
						pessoas.remove(pessoa);
					} catch (CloneNotSupportedException e) {
						e.printStackTrace();
					}
					pessoa = quemEntra(pessoas);
				}
			}else{
				if(acao == SUBIR){
					if(this.andarAtual == 19)
						System.out.println("merda");
					this.andarAtual++;
				}else
					this.andarAtual--;
				this.andaresPercorridos++;
				atualizarDestinos();
			}
		}
	}
	
	public void executarAcao(int acao){
		if(acao == SUBIR)
			this.andarAtual++;
		else
			this.andarAtual--;
		this.andaresPercorridos++;
		if(listaChamadas.contains(andarAtual)){
			listaChamadas.remove(andarAtual);
		}
		atualizarDestinos();
	}
	
	public int getTempoTotalAtendidas() {
		int parcial = 0;
		for (int decorrido : temposEsperaAtendidas) {
			parcial += decorrido;
		}
		return parcial;
	}
	public int getTempoEsperaElevador(){
		int contador = 0;
		for (Pessoa pessoa : pessoas) {
			contador += Math.abs(Orquestrador.contadorTempo - pessoa.getTempo());
		}
		return contador;
	}
	public int getTotalPessoasAtendidas() {
		return temposEsperaAtendidas.size();
	}
	public Queue<Pessoa> getPessoas() {
		return this.pessoas;
	}
	public void setStatus(int status){
		this.status = status;
	}
	public int getStatus(){
		return this.status;
	}
	public void setAcao(int acao){
		this.acao = acao;
	}
	public int getAcao(){
		return this.acao;
	}
	public int getAndarAtual() {
		return this.andarAtual;
	}
	public int getAndaresPercorridos() {
		return this.andaresPercorridos;
	}
	public boolean estaVazio(){
		return this.pessoas.size() == 0;
	}
	public boolean estaLotado(){
		return pessoas.size() == CAPACIDADE_MAX;
	}
	public boolean estaProgramado(){
		return listaChamadas.size() > 0 || listaDestinos.size() > 0;
	}
	public void setChao(int chao){
		this.chao = chao;
	}
	public void setTeto(int teto){
		this.teto = teto;
	}
	public int getChao(){
		return this.chao;
	}
	public int getTeto(){
		return this.teto;
	}
	public void setAndares(ArrayList<Andar> andares){
		this.andares = andares;
	}
	public int getId(){
		return this.id;
	}
	
	public LinkedHashSet<Integer> getListaDestinos() {
		return listaDestinos;
	}
	public void setOrquestrador(Orquestrador o){
		this.orquestrador = o;
	}
	public void setAtendeChamadas(LinkedHashSet<Integer> atendeChamadas ){
		this.atendeChamadas = atendeChamadas;
	}
	/*
	 * garante a entra de pessoas de acordo com a direcao do elevador
	 */
	public boolean alguemEntra() {
		if(this.getStatus()==Elevador.SUBIR){
			return orquestrador.listaChamadasSubida.contains(this.andarAtual);
		}else{
			return orquestrador.listaChamadasDescida.contains(this.andarAtual);
		}
	}
	
	public boolean alguemSai() {
		return listaDestinos.contains(this.andarAtual);
	}
	
	@Override
	public String toString() {
		String aux = String.format("\tId: %d Chao: %d Teto: %d AndarAtual: %d AndaresPercorridos: %d Status: %d \n\tAndaresDestino:[", id, chao, teto, andarAtual, andaresPercorridos, status);
		for (int destino : listaDestinos) {
			aux += destino + ", ";
		}
		aux += String.format("] Chamadas [");
		for (int chamada : listaChamadas) {
			aux += chamada + ", ";
		}
		aux += String.format("] Pessoas [");
		for (Pessoa p : pessoas) {
			aux += p.toString() + ", ";
		}
		aux += String.format("] Atende Chamadas [");
		for (int chamada : atendeChamadas) {
			aux += chamada + ", ";
		}
		return aux + "]\n";
	}
}
