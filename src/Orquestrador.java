import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Orquestrador {

	/*
	 * TODO: 
	 * incrementar tempo de espera das pessoas;
	 * ao remover pessoa adiciona seu tempo a lista de tempos percorridos.
	 * 
	 */
	
	public static final int NUM_ANDARES = 4;
	public static final int NUM_ELEVADORES = 1;
	// número de interações (unidades de tempo)
	public static final int TEMPO_MAX_EXECUCAO = 100;
	// a cada unidade de tempo pode surgir uma nova pessoa em cada andar com uma probabilidade de 50%
	public static final int PROBABILIDADE_CRESCIMENTO_POPULACAO = 1;
	// número inicial de pessoas por andar
	public static final int MAX_PESSOAS_POR_ANDAR = 2;

	public static final int HEURISTICA_DUMMY = 1;
	public static final int HEURISTICA_ENERGIA = 2;
	public static final int HEURISTICA_TEMPO = 3;
	public static final int HEURISTICA_AMBOS = 4;
	
	private double mediaTempos = 0.0;
	private double mediaDistancias = 0.0;
	
	public static int contadorTempo = 0;
	protected ArrayList<Andar> andares;
	protected ArrayList<Elevador> elevadores;
	protected LinkedHashSet<Integer> listaChamadasSubida;
	protected LinkedHashSet<Integer> listaChamadasDescida;
	private int tempoDecorrido = 0 , tempoTotal = 0, pessoasEsperando = 0, pessoasElevador = 0;

	public Orquestrador() {
		contadorTempo = 0;
		listaChamadasSubida = new LinkedHashSet<Integer>();
		listaChamadasDescida = new LinkedHashSet<Integer>();
	}

	public void executar() {
		inicializarCenario();
		do {
			contadorTempo++;
			System.out.printf("\n######### ITERACAO : %d #########\n", contadorTempo);
			atualizarCenario();
			tomarDecisoes();
			executarAcao();
		} while (contadorTempo < TEMPO_MAX_EXECUCAO);
		relatorio();
	}
	
	public void tomarDecisoes(){}

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
	}

/*  
 * distribui as chamadas igualmente para cada elevador cada elevador recebe o total igual a média de chamadas

public void alocarElevadoresPorChamadas(){	 
	LinkedHashSet<Integer> chamadas = new LinkedHashSet<Integer>();
	for (Andar andar : andares) {
		if(!andar.estaVazio())
			chamadas.add(andar.getId());
	}
	double mediaChamadas = (double)chamadas.size()/NUM_ELEVADORES;
	for (Elevador elevador : elevadores) {
		int contador = 0;
		Iterator<Integer> it = chamadas.iterator();
		while (it.hasNext() && contador < mediaChamadas) {
			int chamada = (int) it.next();
			elevador.adicionarChamada(chamada);
			it.remove();
			contador++;
		}
	}
}
*/

	private void executarAcao(){
		for (Elevador elevador : elevadores) {
			elevador.executarAcao();
			System.out.printf("\n###### acao: %d ######\n",elevador.getAcao());
			System.out.println(this);
		}

	}
	private void heuristicaEnergia(){
//		inicializa andares e elevadores
		inicializarCenarioEnergia();
		do {
			contadorTempo++;
			System.out.printf("\n######### INTERACAO : %d #########\n", contadorTempo);

			/*
			 *  distribui as chamadas igualmente para cada elevador
			 *  cada elevador recebe o total igual a média de chamadas
			 */
			LinkedHashSet<Integer> chamadas = new LinkedHashSet<Integer>();
			for (Andar andar : andares) {
				if(!andar.estaVazio())
					chamadas.add(andar.getId());
			}
			double mediaChamadas = (double)chamadas.size()/NUM_ELEVADORES;
			for (Elevador elevador : elevadores) {
				int contador = 0;
				Iterator<Integer> it = chamadas.iterator();
				while (it.hasNext() && contador < mediaChamadas) {
					int chamada = (int) it.next();
					elevador.adicionarChamada(chamada);
					it.remove();
					contador++;
				}
			}
			/*
			 * Para todos os andares, adiciona pessoas nos elevadores disponiveis
			 */
			for (Andar andar : andares) {
				if(!andar.estaVazio()){
					Queue<Pessoa> pessoas = andar.getPessoas();
					ArrayList<Elevador> elevadoresDisponiveis = new ArrayList<Elevador>();
					// verifica quais elevadores estao em cada andar
					for (Elevador elevador : elevadores) {
						int andarAtual = elevador.getAndarAtual();
						if(andar.getId() == andarAtual){
							if(!elevador.estaLotado())
								elevadoresDisponiveis.add(elevador);
						}
					}
					for (Elevador elevador : elevadoresDisponiveis) {
						while(!elevador.estaLotado() && !pessoas.isEmpty()){
							elevador.adicionarPessoa(pessoas.poll());
							pessoasEsperando--;
							pessoasElevador++;
						}
					}
				}
			}
			/*
			 * mover elevadores caso possuam chamadas ou destinos programados
			 */
			for (Elevador elevador : elevadores) {
				if(elevador.estaProgramado()){
					int andarAtual = elevador.getAndarAtual();
					pessoasElevador -= elevador.removerPessoas();
					System.out.println(String.format("###### remover pessoas do andar %d e adicionar no elevador ######", elevador.getAndarAtual()) );
					System.out.println(this);
					if(elevador.getStatus() == Elevador.SUBIR){
						if (andarAtual < elevador.getTeto()){
							elevador.executarAcao(Elevador.SUBIR);	
						}else{
							elevador.executarAcao(Elevador.DESCER);
							elevador.setStatus(Elevador.DESCER);
						}
					}else{
						if (andarAtual > elevador.getChao()){
							elevador.executarAcao(Elevador.DESCER);
						}else{
							elevador.executarAcao(Elevador.SUBIR);
							elevador.setStatus(Elevador.SUBIR);
						}
					}
				}
			}
			atualizarCenario();
		} while (contadorTempo < TEMPO_MAX_EXECUCAO);
		System.out.println("######## ESTADO FINAL ########\n"+this);
			
//		impressao do relatorio final
		relatorio();
	}			

	private void atualizarCenario() {
//		a cada turno adiciona uma nova pessoa em todos os andares
		for (Andar andar : andares) {
			Random random = new Random();
			if(random.nextInt(100) + 1 <= PROBABILIDADE_CRESCIMENTO_POPULACAO){
				andar.adicionarPessoa();
				pessoasEsperando++;
			}
		}
		atualizarChamadas();
	}

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
		for (Andar andar : andares) {
			pessoasEsperando += andar.getPessoas().size();
		}

		System.out.println("###### estado inicial ######");
		System.out.println(this);
	}

	/**
	 * cria o total de NUM_ANDARES de andares;
	 * inicialmente, cada elevador é responsável por atuar entre um intervalo
	 * de andares (chao e teto) alterado conforme destino dos passageiros.
	 * adiciona pessoas aleatoriamente respeitando um MAX_PESSOAS_POR_ANDAR
	 */
	private void inicializarCenarioEnergia() {
		andares = new ArrayList<Andar>();
		elevadores = new ArrayList<Elevador>();

		Random random = new Random();
		for (int i = 0; i < NUM_ANDARES; i++) {
			int totalPessoas = random.nextInt(MAX_PESSOAS_POR_ANDAR);
			andares.add(new Andar(i, totalPessoas));
		}
		for (int i = 0; i < NUM_ELEVADORES; i++) {
			elevadores.add(new Elevador(i, i*(NUM_ANDARES/NUM_ELEVADORES), ((i+1)*(NUM_ANDARES/NUM_ELEVADORES)-1)));
		}
		for (Andar andar : andares) {
			pessoasEsperando+= andar.getPessoas().size();
		}

		System.out.println("###### estado inicial ######");
		System.out.println(this);
	}

	private void relatorio() {
		String relatorio = "\n\n############## RELATORIO FINAL #############\n";
		int totalPessoasAtendidas = 0, totalPessoasEsperando = 0, totalAndaresPercorridos = 0, 
				tempoPessoasAtendidas = 0, tempoPessoasEsperando = 0, tempoPessoasElevador = 0,
				totalPessoasElevador = 0;
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
		double mediaPercorrido = ((double)totalAndaresPercorridos)/elevadores.size();
		relatorio += String.format("Media andares percorridos: %.2f \n",mediaPercorrido);
		
		System.out.println(relatorio);
		
		this.mediaTempos = mediaEspera;
		this.mediaDistancias = mediaPercorrido;
	}

	private LinkedList<Pessoa> retonarTodasPessoas() {
		LinkedList<Pessoa> lista = new LinkedList<Pessoa>();
		for (Andar andar : andares) {
			lista.addAll(andar.getPessoas());
		}
		for(Elevador elevador : elevadores){
			lista.addAll(elevador.getPessoas());
		}
		return lista;
	}

	@Override
	public String toString() {
		String aux = "Andares:\n";
		for (int i = andares.size()-1; i >= 0; i--) {
			aux += andares.get(i);
		}
		aux += "Elevadores:\n";
		for (Elevador e : elevadores) {
			aux += e + "\n";
		}
		aux += "Chamadas Subida: " + listaChamadasSubida + "\n";
		aux += "Chamadas Descida: " + listaChamadasDescida + "\n";
		aux += String.format("Pessoas Esperando: %d, Pessoas no Elevador: %d\n\n", pessoasEsperando, pessoasElevador);
		return aux;
	}

	public double getMediaTempos() {
		return mediaTempos;
	}
	public double getMediaDistancias() {
		return mediaDistancias;
	}
	
	public static void main(String[] args) {
		Orquestrador o = new Dummy();
		o.executar();
	}
}
