import java.util.ArrayList;
import java.util.LinkedList;

public class Orquestrador {

	public static final int NUM_ANDARES = 3;
	public static final int NUM_ELEVADORES = 2;

	private ArrayList<Andar> andares;
	private ArrayList<Elevador> elevadores;
	private int tempoDecorrido;
	private int tempoTotal;

	public Orquestrador() {
		super();
		andares = new ArrayList<Andar>();
		andares.add(new Andar(0, 3));
		andares.add(new Andar(1, 3));
		andares.add(new Andar(2, 3));

		elevadores = new ArrayList<Elevador>();
		Elevador elevador = new Elevador(0);
		elevadores.add(elevador);

		System.out.println("###### estado inicial ######");
		System.out.println(this);

		int totalPessoas = 0;
		do {
			totalPessoas = 0;
			for (Andar andar : andares) {
				totalPessoas += andar.getPessoas().size();

				// remover pessoas do andar 0 e adicionar no elevador
//				Andar andar = andares.get(0);
				LinkedList<Pessoa> pessoas = andar.getPessoas();
				LinkedList<Pessoa> pessoasNoElevador = new LinkedList<Pessoa>();
				for (Pessoa pessoa : pessoas) {
					if (!elevador.estaLotado()) {
						elevador.adicionarPessoa(pessoa);
						pessoasNoElevador.add(pessoa);
					}
				}
				andar.removerPessoas(pessoasNoElevador);
	
				System.out.println("###### remover pessoas do andar "+elevador.getAndarAtual() +" e adicionar no elevador ######");
				System.out.println(this);
	
				// elevador sobe ou desce andar
				if(elevador.getAndarAtual() < Orquestrador.NUM_ANDARES - 1)
					elevador.atualizarStatus(Elevador.SOBE);
				else if(elevador.getAndarAtual() > 0)
					elevador.atualizarStatus(Elevador.DESCE);
				
				System.out.println("###### elevador sobe/desce um andar ######");
				System.out.println(this);
	
				// remover pessoas do elevador
				elevador.removerPessoas(pessoasNoElevador);
				System.out.println("###### remover pessoas do elevador ######");
				System.out.println(this);
			}

/*			// remover pessoas do andar e adicionar no elevador
			andar = andares.get(1);
			pessoas = andar.getPessoas();
			pessoasNoElevador = elevador.getPessoas();
			for (Pessoa pessoa : pessoas) {
				if (!elevador.estaLotado()) {
					elevador.adicionarPessoa(pessoa);
					pessoasNoElevador.add(pessoa);
				}
			}
			andar.removerPessoas(pessoasNoElevador);
			
			System.out.println("###### remover pessoas do andar "+elevador.getAndarAtual() +" e adicionar no elevador ######");
			System.out.println(this);

			// elevador sobe ou desce andar
			if(elevador.getAndarAtual() < Orquestrador.NUM_ANDARES - 1)
				elevador.atualizarStatus(Elevador.SOBE);
			else if(elevador.getAndarAtual() > 0)
				elevador.atualizarStatus(Elevador.DESCE);
			
			System.out.println("###### elevador sobe um andar ######");
			System.out.println(this);

			// remover pessoas do elevador
			elevador.removerPessoas(pessoasNoElevador);
			System.out.println("###### remover pessoas do elevador ######");
			System.out.println(this);

			// remover pessoas do andar e adicionar no elevador
			andar = andares.get(2);
			pessoas = andar.getPessoas();
			pessoasNoElevador = elevador.getPessoas();
			for (Pessoa pessoa : pessoas) {
				if (!elevador.estaLotado()) {
					elevador.adicionarPessoa(pessoa);
					pessoasNoElevador.add(pessoa);
				}
			}
			andar.removerPessoas(pessoasNoElevador);
			
			System.out.println("###### remover pessoas do andar "+elevador.getAndarAtual() +" e adicionar no elevador ######");
			System.out.println(this);

			// elevador sobe ou desce andar
			if(elevador.getAndarAtual() < Orquestrador.NUM_ANDARES - 1)
				elevador.atualizarStatus(Elevador.SOBE);
			else if(elevador.getAndarAtual() > 0)
				elevador.atualizarStatus(Elevador.DESCE);
			
			System.out.println("###### elevador sobe um andar ######");
			System.out.println(this);

			// remover pessoas do elevador
			elevador.removerPessoas(pessoasNoElevador);
			System.out.println("###### remover pessoas do elevador ######");
			System.out.println(this);

			// remover pessoas do andar e adicionar no elevador
			andar = andares.get(1);
			pessoas = andar.getPessoas();
			pessoasNoElevador = elevador.getPessoas();
			for (Pessoa pessoa : pessoas) {
				if (!elevador.estaLotado()) {
					elevador.adicionarPessoa(pessoa);
					pessoasNoElevador.add(pessoa);
				}
			}
			andar.removerPessoas(pessoasNoElevador);
			
			System.out.println("###### remover pessoas do andar "+elevador.getAndarAtual() +" e adicionar no elevador ######");
			System.out.println(this);

			// elevador sobe ou desce andar
			if(elevador.getAndarAtual() < Orquestrador.NUM_ANDARES - 1)
				elevador.atualizarStatus(Elevador.SOBE);
			else if(elevador.getAndarAtual() > 0)
				elevador.atualizarStatus(Elevador.DESCE);
			
			System.out.println("###### elevador sobe um andar ######");
			System.out.println(this);

			// remover pessoas do elevador
			elevador.removerPessoas(pessoasNoElevador);
			System.out.println("###### remover pessoas do elevador ######");
			System.out.println(this);

			// remover pessoas do andar e adicionar no elevador
			andar = andares.get(0);
			pessoas = andar.getPessoas();
			pessoasNoElevador = elevador.getPessoas();
			for (Pessoa pessoa : pessoas) {
				if (!elevador.estaLotado()) {
					elevador.adicionarPessoa(pessoa);
					pessoasNoElevador.add(pessoa);
				}
			}
			andar.removerPessoas(pessoasNoElevador);
			
			System.out.println("###### remover pessoas do andar "+elevador.getAndarAtual() +" e adicionar no elevador ######");
			System.out.println(this);

			// elevador sobe ou desce andar
			if(elevador.getAndarAtual() < Orquestrador.NUM_ANDARES - 1)
				elevador.atualizarStatus(Elevador.SOBE);
			else if(elevador.getAndarAtual() > 0)
				elevador.atualizarStatus(Elevador.DESCE);
			
			System.out.println("###### elevador sobe um andar ######");
			System.out.println(this);

			// remover pessoas do elevador
			elevador.removerPessoas(pessoasNoElevador);
			System.out.println("###### remover pessoas do elevador ######");
			System.out.println(this);
*/
			
		} while (totalPessoas != 0);

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
		return aux;
	}

}
