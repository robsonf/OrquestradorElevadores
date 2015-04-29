import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public abstract class Orquestrador {

	/*
	 * TODO: 
	 * 
	 */
	
	public static final int NUM_ANDARES = 20;
	public static final int NUM_ELEVADORES = 10;
	// número de interações (unidades de tempo)
	public static final int TEMPO_MAX_EXECUCAO = 1000;
	// a cada unidade de tempo pode surgir uma nova pessoa em cada andar com uma probabilidade de 50%
	public static final int PROBABILIDADE_CRESCIMENTO_POPULACAO = 1;
	// número inicial de pessoas por andar
	public static final int MAX_PESSOAS_POR_ANDAR = 2;

	public static final boolean exibirLogs = false;
	
	private double mediaTempos = 0.0;
	private double mediaDistancias = 0.0;
	
	public static int contadorTempo = 0;
	protected ArrayList<Andar> andares;
	protected ArrayList<Elevador> elevadores;
	protected LinkedHashSet<Integer> listaChamadasSubida;
	protected LinkedHashSet<Integer> listaChamadasDescida;

//	public Orquestrador() {
//	}
	
	public void inicializar(){
		contadorTempo = 0;
		listaChamadasSubida = new LinkedHashSet<Integer>();
		listaChamadasDescida = new LinkedHashSet<Integer>();
		executar();
	}
	public void executar() {
		inicializarCenario();
		do {
			contadorTempo++;
			if(exibirLogs){			
				System.out.printf("######### ITERACAO : %d #########", contadorTempo);
			}
			atualizarCenario();
			tomarDecisoes();
			executarAcao();
		} while (contadorTempo < TEMPO_MAX_EXECUCAO);
		relatorio();
	}
	
	public abstract void tomarDecisoes();

	private void executarAcao(){
		for (Elevador elevador : elevadores) {
			elevador.executarAcao();
			if(exibirLogs){
				System.out.printf("\n###### acao: %d ######\n",elevador.getAcao());
				System.out.println(this);
			}
		}
	}
	
	private void atualizarCenario() {
//		a cada turno adiciona uma nova pessoa em todos os andares
		for (Andar andar : andares) {
			Random random = new Random();
			if(random.nextInt(100) + 1 <= PROBABILIDADE_CRESCIMENTO_POPULACAO){
				andar.adicionarPessoa();
			}
		}
		atualizarChamadas();
	}

	/*
	 *  atualizar listas de chamadas, não aloca elevadores
	 */
	public void atualizarChamadas(){
		listaChamadasSubida = new LinkedHashSet<Integer>();
		listaChamadasDescida = new LinkedHashSet<Integer>();
		for (Andar andar : andares) {
			if(!andar.estaVazio()){
				Queue<Pessoa> pessoas = andar.getPessoas();
				for (Pessoa pessoa : pessoas) {
					if(pessoa.getDirecao() == Elevador.SUBIR)
						listaChamadasSubida.add(andar.getId());
					else
						listaChamadasDescida.add(andar.getId());
				}
			}
		}
		notificarAtualizacaoChamadas();
	}
	
	public void notificarAtualizacaoChamadas(){}

	/**
	 * cria o total de NUM_ANDARES de andares;
	 * adiciona pessoas aleatoriamente respeitando um MAX_PESSOAS_POR_ANDAR
	 */
	private void inicializarCenario() {
		andares = new ArrayList<Andar>();
		elevadores = new ArrayList<Elevador>();

		Random random = new Random();
		for (int i = 0; i < NUM_ANDARES; i++) {
			int totalPessoas = random.nextInt(MAX_PESSOAS_POR_ANDAR);
			andares.add(new Andar(i, totalPessoas));
		}
		for (int i = 0; i < NUM_ELEVADORES; i++) {
			Elevador e = new Elevador(i);
			e.setAndares(andares);
			e.setOrquestrador(this);
			elevadores.add(e);
		}
		if(exibirLogs){
			System.out.println("###### estado inicial ######");
			System.out.println(this);
		}
	}

	private void relatorio() {
		String relatorio = "\n\n############## RELATORIO PARCIAL #############\n";
		int totalPessoasAtendidas = 0, totalPessoasEsperando = 0, totalAndaresPercorridos = 0, 
				tempoPessoasAtendidas = 0, tempoPessoasEsperando = 0, tempoPessoasElevador = 0,
				totalPessoasElevador = 0, totalEnergiaPessoasEsperando = 0;
		for(Elevador elevador : elevadores){
			int parcialAndares = elevador.getAndaresPercorridos();
			relatorio += String.format("Total Percorrido Elevador[%d] = %d \n",elevador.getId(), parcialAndares);
			totalAndaresPercorridos += parcialAndares;
			tempoPessoasAtendidas += elevador.getTempoTotalAtendidas();
			totalPessoasAtendidas += elevador.getTotalPessoasAtendidas();
			tempoPessoasElevador += elevador.getTempoEsperaElevador();
			totalPessoasElevador += elevador.getPessoas().size();
		}

		for (Andar andar : andares) {
			tempoPessoasEsperando += andar.tempoPessoasEsperando();
			totalPessoasEsperando += andar.getPessoas().size();
			totalEnergiaPessoasEsperando += andar.energiaPessoasEsperando();
		}
		relatorio += String.format("\nTotal andares percorridos: %d \n",totalAndaresPercorridos);
		relatorio += String.format("Total pessoas atendidas: %d \n",totalPessoasAtendidas);
		relatorio += String.format("Total pessoas no elevador: %d \n",totalPessoasElevador);
		relatorio += String.format("Total pessoas esperando: %d \n",totalPessoasEsperando);
		relatorio += String.format("Tempo total pessoas atendidas: %d \n",tempoPessoasAtendidas);
		relatorio += String.format("Tempo total pessoas elevador: %d \n",tempoPessoasElevador);
		relatorio += String.format("Tempo total pessoas esperando: %d \n",tempoPessoasEsperando);
		double mediaEspera = ((double)(tempoPessoasAtendidas+tempoPessoasEsperando+tempoPessoasElevador))/
				(totalPessoasAtendidas+totalPessoasElevador+totalPessoasEsperando);
		relatorio += String.format("\nMedia de espera: %.2f \n", mediaEspera);
		double mediaPercorrido = (((double)totalAndaresPercorridos)/elevadores.size()) + (totalEnergiaPessoasEsperando/elevadores.size());
		relatorio += String.format("Media andares percorridos: %.2f \n",mediaPercorrido);
		
		if(exibirLogs){
			System.out.println(relatorio);
		}
		
		this.mediaTempos = mediaEspera;
		this.mediaDistancias = mediaPercorrido;
	}

	@Override
	public String toString() {
		String aux = "Andares:\n";
		for (int i = andares.size()-1; i >= 0; i--) {
			aux += andares.get(i);
		}
		aux += "Elevadores:";
		for (Elevador e : elevadores) {
			aux += "\n" + e;
		}
		aux += "Chamadas Subida: " + listaChamadasSubida + "\n";
		aux += "Chamadas Descida: " + listaChamadasDescida + "\n";
		return aux;
	}

	public double getMediaTempos() {
		return mediaTempos;
	}
	public double getMediaDistancias() {
		return mediaDistancias;
	}
	
	public static void main(String[] args) {
//		Orquestrador o = new Dummy();
		Orquestrador o = new ReducaoEnergia();
//		Orquestrador o = new BaseLine();
	}
}
