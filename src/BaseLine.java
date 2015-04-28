import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;

/** 
 * 
 */

public class BaseLine extends Orquestrador {

	public BaseLine() {
		super();
	}

	public void tomarDecisoes() {
		for (Elevador elevador : elevadores) {
			int andarAtual = elevador.getAndarAtual();
			// condicao de parada do elevador
			if (listaChamadasDescida.isEmpty() && listaChamadasSubida.isEmpty() && elevador.getPessoas().isEmpty()) {
				elevador.setAcao(Elevador.PARAR);
				elevador.setStatus(Elevador.PARAR);
				continue;
			}
			// alterar o teto e chao de acordo com as chamadas e destinos do usuario
			atualizarLimites(elevador);
			// se parado, direciona o elevador para a mesma direcao do maior número de chamadas
			if(elevador.getStatus() == Elevador.PARAR){
				if(!listaChamadasDescida.isEmpty()){
					if(!listaChamadasSubida.isEmpty()){
						if(listaChamadasDescida.size()<listaChamadasSubida.size())
							elevador.setStatus(Elevador.SUBIR);
							continue;
					}
					elevador.setStatus(Elevador.DESCER);
					continue;
				}
				if(!listaChamadasSubida.isEmpty()){
					elevador.setStatus(Elevador.SUBIR);
					continue;
				}
			}
			if (elevador.getStatus() == Elevador.SUBIR) {
				if (andarAtual < elevador.getTeto()) {
					elevador.setAcao(Elevador.SUBIR);
				} else {
					elevador.setAcao(Elevador.DESCER);
					elevador.setStatus(Elevador.DESCER);
				}
			} else {
				if (andarAtual > elevador.getChao()) {
					elevador.setAcao(Elevador.DESCER);
				} else {
					elevador.setAcao(Elevador.SUBIR);
					elevador.setStatus(Elevador.SUBIR);
				}
			}
			// elevador para caso alguem sobe ou desce
			if (elevador.alguemSai() || (elevador.alguemEntra() && !elevador.estaLotado())){
				elevador.setAcao(Elevador.PARAR);
				continue;
			}
		}
	}

	private void atualizarLimites(Elevador elevador) {
		ArrayList<Integer> limites = new ArrayList<Integer>();
		adicionaLimites(listaChamadasDescida, limites);
		adicionaLimites(listaChamadasSubida, limites);
		adicionaLimites(elevador.getListaDestinos(), limites);
		if(limites.isEmpty())
			limites.add(0);
		Collections.sort(limites);
		elevador.setChao(limites.get(0));
		elevador.setTeto(limites.get(limites.size()-1));
	}
	public void adicionaLimites(LinkedHashSet<Integer> lista, ArrayList<Integer> limites){
		if (!lista.isEmpty()) {
			Object[] aux = lista.toArray();
			Arrays.sort(aux);
			limites.add((int) aux[0]);
			limites.add((int) aux[aux.length - 1]);
		}
	}
}
