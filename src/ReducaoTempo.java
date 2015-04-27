import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;

/** 
 * 
 */

public class ReducaoTempo extends Orquestrador {

	public ReducaoTempo() {
		super();
	}

	public void tomarDecisoes(){
		for (Elevador elevador : elevadores) {
			int andarAtual = elevador.getAndarAtual();
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
