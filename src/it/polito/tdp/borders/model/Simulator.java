package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulator {

	// Tipi di Eventi / coda degli eventi (cosa fa evolvere? Il fatto che le persone si spostano, dove vanno (da dove vengono non mi interessa))
	PriorityQueue<Event> queue; // cosda prioritaria di eventi
	
	// Modello del mondo (parte statica -> evento stati con numero di persone stanziate in quello stato) 
	private Map<Country, Integer> stanziali; //-> parte da 0 e poi incrementa
	private Graph<Country, DefaultEdge> graph;
	
	// Parametri di simulazione (numero di persone che vengono a t=0)
	private int N_MIGRANTI = 1000;
	
	// Valori in output
	private int T; // passi simulazione
	
	public void init(Graph<Country, DefaultEdge> graph, Country partenza) { // Indicare il grafo delle nazioni (devo sapere sempre quali sono le nazioni confinanti) e lo stato iniziale da cui partire
		// creare coda eventi, azzera tempo e mappa degli stanziali
		this.T = 1;
		this.stanziali = new HashMap<Country, Integer>();
		for(Country c : graph.vertexSet()) { // ogni elemento della mappa deve essere inizializzato a 0
			this.stanziali.put(c, 0);
		}
		this.queue = new PriorityQueue<Event>();
		
		this.queue.add(new Event(T,this.N_MIGRANTI , partenza)); // al t = 1 arriveranno 1000 migranti
		this.graph = graph;
		
		
	}
	
	
	public void run() {
		Event e;
		while((e=queue.poll()) != null) {
			
			this.T = e.getT(); // così in e avrò l'ultimo T -> ovvero dell'ultimo che ho estratto (con T più alto).
			
			int arrivi = e.getNum();
			Country stato = e.getDestination();
			
			// dobbiamo dividere gli arrivi in meta stanziale e meta non
			List<Country> confinanti = Graphs.neighborListOf(this.graph, stato); // mi faccio dire gli stati confinanti dal mio grafo
			
			int migranti = (arrivi / 2) / confinanti.size(); // 80 arrivi -> 40 non stanziali -> li divido in 3 confinanti
			
			if(migranti != 0) {
				// genero gli eventi di arrivo di questi migranti in ciascuno degli stati confinanti
				for(Country arrivo : confinanti) {
					this.queue.add(new Event (e.getT()+1, migranti, arrivo));
				}
			}
			
			int rimasti = arrivi - migranti * confinanti.size(); // a meno di errori di arrotondamento saranno la meta
			
			this.stanziali.put(stato, this.stanziali.get(stato)+rimasti); // aggiorno gli stanziali 
			
			
			
		}
	}


	public Map<Country, Integer> getStanziali() {
		return stanziali;
	}


	public int getT() {
		return T;
	}
}
