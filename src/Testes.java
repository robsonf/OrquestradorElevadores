import java.util.ArrayList;


public class Testes {
	// número de baterias de testes a serem executados
	public static final int TOTAL_BATERIAS = 10;
	
	public static void main(String[] args) {
/*		ArrayList<Double> mediaTempos = new ArrayList<Double>();
		ArrayList<Double> mediaDistancias = new ArrayList<Double>();
		double acumulaTempo = 0.0;
		double acumulaDistancia = 0.0;
		
		ArrayList<Orquestrador> testes = new ArrayList<Orquestrador>(); 
		for (int i = 0; i < TOTAL_BATERIAS; i++) {
			testes.add(new Orquestrador(Orquestrador.HEURISTICA_DUMMY));
//			testes.add(new Orquestrador(Orquestrador.HEURISTICA_ENERGIA));
		}
		for (Orquestrador o : testes) {
			mediaTempos.add(o.getMediaTempos());
			mediaDistancias.add(o.getMediaDistancias());
			acumulaTempo += o.getMediaTempos();
			acumulaDistancia += o.getMediaDistancias();
		}
		System.out.printf("\n\n\n########## RELATORIO FINAL ##############\n");
		System.out.printf("Media tempo: %.2f\n", acumulaTempo/TOTAL_BATERIAS);
		System.out.printf("Media distancia: %.2f\n", acumulaDistancia/TOTAL_BATERIAS);
*/
		Orquestrador o = new Dummy();
		o.executar();
		
	}
}
