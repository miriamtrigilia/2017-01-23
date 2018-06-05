/**
 * Skeleton for 'Borders.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.CountryAndNumber;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class BordersController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxNazione"
    private ComboBox<Country> boxNazione; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    private Model model;

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    		String annoS = this.txtAnno.getText();
    		try { 
    			int anno = Integer.parseInt(annoS);
    			model.creaGrafo(anno);
    			
    			List<CountryAndNumber> list = model.getCountryAndNumber();
    			
    			if(list.size() == 0)
    				this.txtResult.appendText("Non ci sono dati corrispondenti");
    			else {
    				this.txtResult.appendText("Stati nell'anno :"+anno+"\n");
    				for(CountryAndNumber c : list) {
	    				this.txtResult.appendText(c.getCountry().getStateName()+ " "+ c.getNumber()+"\n");;
	    			}
    			}
    			
    			 // Aggiorn a la tendina con gli stati presenti nel grafo
    			this.boxNazione.getItems().clear(); // magari è la 3 terza volta che chiamo questo metodo
    			this.boxNazione.getItems().addAll(model.getCountries()); // le voglio ordinate per Nome
    		} catch(NumberFormatException e) {
    			this.txtResult.appendText("Errore di formattazione dell'anno");
    			return;
    		}
    		
    		
    }

    @FXML
    void doSimula(ActionEvent event) {
    		Country partenza = this.boxNazione.getValue(); // può essere null
    		if(partenza == null) {
    			this.txtResult.appendText("ERRORE: selezionare una nazione\n");
    			return;
    		}
    		
    		this.model.simula(partenza); // sulla base della simulazione chiederò il Tmax e il numero di simulazioni
    		int simT = this.model.getTsimulazione();
    		List<CountryAndNumber> stanziali = this.model.getCountriesStanziali();
     		
    		this.txtResult.appendText("Simulazione dallo stato "+partenza+"\n");
    		this.txtResult.appendText("Durata: "+simT+" passi\n");
    		for(CountryAndNumber cn : stanziali) {
    			if(cn.getNumber()!=0) 
    				this.txtResult.appendText("Nazione "+cn.getCountry().getStateAbb()+ "-"+cn.getCountry().getStateName()+" Stanziali="+cn.getNumber()+"\n");
    		}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Borders.fxml'.";
        assert boxNazione != null : "fx:id=\"boxNazione\" was not injected: check your FXML file 'Borders.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Borders.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		
	}
}
