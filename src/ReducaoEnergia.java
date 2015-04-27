import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;

/** 
 * Para cada elevador se não há chamadas ou pessoas dentro do elevador
 * então permanece parado. Caso acao subir, continuar subindo, 
 * chegando no último andar, descer. Se descer, continuar descendo, chegando 
 * no primeiro andar, subir.
 */

public class ReducaoEnergia extends Orquestrador {

	public ReducaoEnergia() {
		super();
	}

	public void tomarDecisoes(){
		for (Elevador elevador : elevadores) {
			int andarAtual = elevador.getAndarAtual();
			if(listaChamadasDescida.isEmpty() && listaChamadasDescida.isEmpty() && elevador.getPessoas().isEmpty()){
				  elevador.setAcao(Elevador.PARAR);
				  continue;

			}
			// se em movimento entao continua em movimento
			if(elevador.getStatus() == Elevador.SUBIR){
				if (andarAtual < elevador.getTeto()){
					elevador.setAcao(Elevador.SUBIR);	
				}else{
					elevador.setAcao(Elevador.DESCER);
					elevador.setStatus(Elevador.DESCER);
				}
			}else{
				if (andarAtual >  elevador.getChao()){
					elevador.setAcao(Elevador.DESCER);
				}else{
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
}
