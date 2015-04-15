import java.util.ArrayList;
import java.util.LinkedList;

public class Orquestrador {

	public static final int NUM_ANDARES = 4;
	public static final int NUM_ELEVADORES = 1;

	private ArrayList<Andar> andares;
	private ArrayList<Elevador> elevadores;
	private int tempoDecorrido = 0 , tempoTotal = 0, pessoasEsperando = 0, pessoasElevador = 0;
	public Orquestrador() {
		super();
		andares = new ArrayList<Andar>();
		elevadores = new ArrayList<Elevador>();
		for (int i = 0; i < NUM_ANDARES; i++) {
			andares.add(new Andar(i, 6));
		}
		for (int i = 0; i < NUM_ELEVADORES; i++) {
			elevadores.add(new Elevador(i));
		}

		/*
		 * TODO: para cada elevador verificar acao, se subir, continuar subindo, chegando no último andar, descer.
		 * 		Se descendo, continuar descer, chegando no primeiro andar, subir.
		 */
		
		for (Andar andar : andares) {
			pessoasEsperando+= andar.getPessoas().size();
		}

		System.out.println("###### estado inicial ######");
		System.out.println(this);

		do {
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

//					mover o elevador com heuristica dummy
				if(elevador.getStatus() == Elevador.SUBIR){
					if (andarAtual < Orquestrador.NUM_ANDARES - 1){
						elevador.executarAcao(Elevador.SUBIR);	
					}else{
						elevador.executarAcao(Elevador.DESCER);
						elevador.setStatus(Elevador.DESCER);
					}
				}else{
//				if(elevador.getStatus() == Elevador.DESCER){
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
			
		} while (!(pessoasEsperando == 0 && pessoasElevador == 0));

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
		aux += String.format("Esperando: %d, Elevador: %d\n\n", pessoasEsperando, pessoasElevador);
		return aux;
	}

}
