import java.util.ArrayList;
import java.util.LinkedList;

public class Orquestrador {

	public static final int NUM_ANDARES = 3;
	public static final int NUM_ELEVADORES = 2;

	private ArrayList<Andar> andares;
	private ArrayList<Elevador> elevadores;
	private int tempoDecorrido;
	private int tempoTotal;
	private int pessoasEsperando = 	0, pessoasElevador = 0;
	public Orquestrador() {
		super();
		andares = new ArrayList<Andar>();
		andares.add(new Andar(0, 3));
		andares.add(new Andar(1, 3));
		andares.add(new Andar(2, 3));

		elevadores = new ArrayList<Elevador>();
		Elevador e1 = new Elevador(0);
		elevadores.add(e1);


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
//				remover pessoas do andar 0 e adicionar no elevador
				System.out.println("###### remover pessoas do andar "+elevador.getAndarAtual() +" e adicionar no elevador ######");
				System.out.println(this);
	
				// elevador sobe ou desce andar
				if(elevador.getStatus() == Elevador.SUBIR){
					if (andarAtual < Orquestrador.NUM_ANDARES - 1){
						elevador.atualizarAndar(Elevador.SUBIR);	
					}else{
						elevador.atualizarAndar(Elevador.DESCER);	
					}
				}
				if(elevador.getStatus() == Elevador.DESCER){
					if (andarAtual > 0){
						elevador.atualizarAndar(Elevador.DESCER);
					}else{
						elevador.atualizarAndar(Elevador.SUBIR);
					}
				}
				
				System.out.println("###### elevador sobe/desce um andar ######");
				System.out.println(this);
	
				// remover pessoas do elevador
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
		// String aux =
		// String.format("TempoDecorrido: %d TempoTotal: %d\nAndares:\n",
		// tempoDecorrido, tempoTotal);
		String aux = "Andares:\n";
		for (Andar a : andares) {
			aux += a;
		}
		aux += "Elevadores:\n";
		for (Elevador e : elevadores) {
			aux += e + "\n";
		}
		aux += String.format("Esperando: %d, Elevador: %d\n\n", pessoasEsperando, pessoasElevador);
		return aux;
	}

}
