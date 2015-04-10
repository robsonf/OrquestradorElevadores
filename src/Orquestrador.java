import java.util.ArrayList;

public class Orquestrador {

	public static final int NUM_ANDARES = 3;
	public static final int NUM_ELEVADORES = 2;

	private ArrayList<Andar> andares;
	private ArrayList<Elevador> elevadores;
	private int tempoDecorrido;
	private int tempoTotal;

	public Orquestrador() {
		super();
		andares = new ArrayList<Andar>(3);
		andares.add(new Andar(0,3));
		andares.add(new Andar(1,3));
		andares.add(new Andar(2,3));

		elevadores = new ArrayList<Elevador>(1);
		Elevador elevador = new Elevador(0);
		elevadores.add(elevador);
		
		for(Andar andar : andares){
			for(int i = 0; i < NUM_ANDARES; i++){
				boolean [] aux = andar.getDestinos();
				elevador.adicionarDestino(i);
			}
		}
	}
	
	public static void main(String[] args) {
		Orquestrador o = new Orquestrador();
		System.out.println(o);
	}
	
	@Override
	public String toString() {
		String aux = String.format("TempoDecorrido: %d TempoTotal: %d\nAndares:\n", tempoDecorrido, tempoTotal);
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
