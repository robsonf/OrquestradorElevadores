import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Orquestrador {

	/*
	 * TODO: 
	 * incrementar tempo de espera das pessoas
	 * ao remover pessoa adiciona seu tempo a lista de tempos percorridos.
	 * 
	 */
	
	public static final int NUM_ANDARES = 3;
	public static final int NUM_ELEVADORES = 1;
	// número máximo de pessoas por andar inicialmente
	public static final int MAX_PESSOAS_POR_ANDAR = 1;
	public static final int TEMPO_MAX_EXECUCAO = 1000;
	public static final int PROBABILIDADE_CRESCIMENTO_POPULACAO = 50;
	
	public static int contadorTempo = 0;
	private ArrayList<Andar> andares;
	private ArrayList<Elevador> elevadores;
	private int tempoDecorrido = 0 , tempoTotal = 0, pessoasEsperando = 0, pessoasElevador = 0;
	public Orquestrador() {
		super();
		andares = new ArrayList<Andar>();
		elevadores = new ArrayList<Elevador>();

		/*
		 *  cria o total de NUM_ANDARES de andares;
		 *  adiciona pessoas aleatoriamente respeitando um MAX_PESSOAS_POR_ANDAR
		 */
		Random random = new Random();
		for (int i = 0; i < NUM_ANDARES; i++) {
			int totalPessoas = random.nextInt(MAX_PESSOAS_POR_ANDAR);
			andares.add(new Andar(i, totalPessoas));
		}
		for (int i = 0; i < NUM_ELEVADORES; i++) {
			elevadores.add(new Elevador(i));
		}

		
		for (Andar andar : andares) {
			pessoasEsperando+= andar.getPessoas().size();
		}

		System.out.println("###### estado inicial ######");
		System.out.println(this);

		do {
			contadorTempo++;
			System.out.printf("\n######### INTERACAO : %d #########\n", contadorTempo);

			for (Elevador elevador : elevadores) {
				int andarAtual = elevador.getAndarAtual();
				Andar andar = andares.get(andarAtual);
				LinkedList<Pessoa> pessoas = andar.getPessoas();
				
				while(!elevador.estaLotado() && !pessoas.isEmpty()){
					elevador.adicionarPessoa(pessoas.poll());
					pessoasEsperando--;
					pessoasElevador++;
				}
//					remover pessoas do andar atual e adicionar no elevador
				System.out.println(String.format("###### remover pessoas do andar %d e adicionar no elevador ######", elevador.getAndarAtual()) );
				System.out.println(this);

				/*
				 * Mover o elevador com heuristica dummy: 
				 * Para cada elevador verificar acao, se subir, continuar subindo, 
				 * chegando no último andar, descer. Se descer, continuar descendo, chegando 
				 * no primeiro andar, subir.
				 */

				if(elevador.getStatus() == Elevador.SUBIR){
					if (andarAtual < Orquestrador.NUM_ANDARES - 1){
						elevador.executarAcao(Elevador.SUBIR);	
					}else{
						elevador.executarAcao(Elevador.DESCER);
						elevador.setStatus(Elevador.DESCER);
					}
				}else{
					if (andarAtual > 0){
						elevador.executarAcao(Elevador.DESCER);
					}else{
						elevador.executarAcao(Elevador.SUBIR);
						elevador.setStatus(Elevador.SUBIR);
					}
				}
				
//				 remover pessoas do elevador
				pessoasElevador -= elevador.removerPessoas();
				System.out.println("###### remover pessoas do elevador ######");
				System.out.println(this);
				
			}
			
//			a cada turno adiciona uma nova pessoa em todos os andares
			for (Andar andar : andares) {
				random = new Random();
				if(random.nextInt(100) + 1 <= PROBABILIDADE_CRESCIMENTO_POPULACAO){
					andar.adicionarPessoas();
					pessoasEsperando++;
				}
			}

			
		} while (contadorTempo < TEMPO_MAX_EXECUCAO);
//	} while (!(pessoasEsperando == 0 && pessoasElevador == 0) && contadorTempo < TEMPO_MAX_EXECUCAO);

		System.out.println("######## ESTADO FINAL ########\n"+this);
//		impressao do relatorio final
		relatorio();
	}

	private void relatorio() {
		String relatorio = "\n\n############## RELATORIO FINAL #############\n";
		int totalPessoasAtendidas = 0, totalPessoasEsperando = 0, totalAndaresPercorridos = 0, 
				tempoPessoasAtendidas = 0, tempoPessoasEsperando = 0, tempoPessoasElevador = 0;
		for (int i = 0; i < elevadores.size(); i++) {
			Elevador elevador = elevadores.get(i);
			int parcialAndares = elevador.getAndaresPercorridos();
			relatorio += String.format("id:%d , total Percorrido:%d \n",i, parcialAndares);
			totalAndaresPercorridos += parcialAndares;
			tempoPessoasAtendidas += elevador.getTempoTotalAtendidas();
			totalPessoasAtendidas += elevador.getTotalPessoasAtendidas();
			tempoPessoasElevador += elevador.getTempoEsperaElevador();
		}

		for (Andar andar : andares) {
			tempoPessoasEsperando += andar.tempoPessoasEsperando();
			totalPessoasEsperando += andar.getPessoas().size();
		}
		relatorio += String.format("\nTotal pessoas atendidas:%d \n",totalPessoasAtendidas);
		relatorio += String.format("Total pessoas no elevador:%d \n",pessoasElevador);
		relatorio += String.format("Total pessoas esperando:%d \n",totalPessoasEsperando);
		relatorio += String.format("Tempo total pessoas atendidas:%d \n",tempoPessoasAtendidas);
		relatorio += String.format("Tempo total pessoas elevador:%d \n",tempoPessoasElevador);
		relatorio += String.format("Tempo total pessoas esperando:%d \n",tempoPessoasEsperando);
		double mediaEspera = ((double)(tempoPessoasAtendidas+tempoPessoasEsperando+tempoPessoasElevador))/
				(totalPessoasAtendidas+pessoasElevador+totalPessoasEsperando);
		relatorio += String.format("Media de espera:%.2f \n", mediaEspera);
		
		System.out.println(relatorio);
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

	public static void main(String[] args) {
		Orquestrador o = new Orquestrador();
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
		aux += String.format("Pessoas Esperando: %d, Pessoas no Elevador: %d\n\n", pessoasEsperando, pessoasElevador);
		return aux;
	}

}
