package com.mbds.material.PatrouilleNFC.Utils;

import com.mbds.material.PatrouilleNFC.Model.InfoPersonne;
import com.mbds.material.PatrouilleNFC.Model.Infraction;
import com.mbds.material.PatrouilleNFC.Model.Personne;
import com.mbds.material.PatrouilleNFC.Model.TypeInfraction;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Safidimahefa on 14/04/2016.
 */
public class Utilitaire {
    private static final String separator="-";

    private static final String SERVEUR = "http://policenationale.esy.es/";
    //private static final String SERVEUR = "http://192.168.1.7:8888/WebServiceNFC/index.php/";
    public static String getRequest(String uri) throws Exception{
        InputStream is = null;
        String result = null;
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet httpGet = new HttpGet(uri);

        HttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();

        is = entity.getContent();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        result = sb.toString();
        return result;

    }
    public static String parseUserJson(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);

            int idpersonne =  jsonObject.getInt("ID_PERSONNE");
            String typeutilisateur = jsonObject.getString("TYPE");
            int idutilitsateur = jsonObject.getInt("ID_UTILISATEUR");
            InputStream is = null;
            String result = null;
            try{
                result = getRequest(SERVEUR+"nfcControler/personneById/"+idpersonne);
                JSONObject jsonObject2 = new JSONObject(result);
                String numeroTag = jsonObject2.getString("NUMERO_TAG");
                String nomPrenom = jsonObject2.getString("NOM") + " " + jsonObject2.getString("PRENOM");
                return  nomPrenom + getSeparator() + numeroTag + getSeparator() + idpersonne + getSeparator() + typeutilisateur + getSeparator() + idutilitsateur ;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean checkValidPerson(String idtag) throws Exception {
        String result = getRequest(SERVEUR+"nfcControler/getAllInfoPersonneByTag/" + idtag);
        if (result == null || result.equals("echec") || result.isEmpty())
            return false;
        return true;

    }

    public static String getPersonneTagById(int id) throws Exception{
        String result = getRequest(SERVEUR+"nfcControler/personneById/"+id);
        if(testJson(result)){
            JSONObject jsonObject = new JSONObject(result);
            return jsonObject.getString("NUMERO_TAG");
        }
        return null;

    }

    public static InfoPersonne bindInfoPersonneByTag(String idtag) throws Exception{
        String result = getRequest(SERVEUR+"nfcControler/getAllInfoPersonneByTag/"+idtag);
            if(result==null || result.equals("echec") || result.isEmpty())
                throw new Exception(" Tag invalide ");
            JSONObject jsonObject = new JSONObject(result);

            String jsonpersonne = jsonObject.getString("personne");

            JSONObject pers = new JSONObject(jsonpersonne);

            InfoPersonne personne = new InfoPersonne();
            personne.setId_personne(pers.getInt("ID_PERSONNE"));
            personne.setNom(pers.getString("NOM"));
            personne.setPrenom(pers.getString("PRENOM"));

            int se = pers.getInt("SEXE");
            if(se == 1){
                personne.setSexe("Homme");
            }
            else
                personne.setSexe("Femme");
            personne.setDate_naiss(pers.getString("DATE_NAISS"));
            personne.setAdresse(pers.getString("ADRESSE"));
            personne.setImage(pers.getString("IMAGE"));
            personne.setLieu_naiss(pers.getString("LIEU_NAISS"));
            personne.setImmatriculation(pers.getString("IMMATRICULATION"));
            personne.setProfession(pers.getString("PROFESSION"));
            personne.setNom_pere(pers.getString("NOM_PERE"));
            personne.setNom_mere(pers.getString("NOM_MERE"));
            String situation = pers.getString("SITUATION_MAT");
            if(situation !=null && situation.equals("marie"))
                situation = "Marié(e)";
            if(situation !=null && situation.equals("celibataire"))
                situation = "Célibataire";
            personne.setSituation_mat(situation);
            personne.setStatut(pers.getString("STATUT"));
            personne.setNumero_tag(pers.getString("NUMERO_TAG"));


            String jsonpermis = jsonObject.getString("permis_conduire");
           if(testJson(jsonpermis)) {
               JSONObject perm = new JSONObject(jsonpermis);
               personne.setDate_permis(perm.getString("DATE_PERMIS"));
               personne.setCategorie_a(perm.getInt("CATEGORIE_A"));
               personne.setCategorie_b(perm.getInt("CATEGORIE_B"));
               personne.setCategorie_c(perm.getInt("CATEGORIE_C"));
               personne.setCategorie_d(perm.getInt("CATEGORIE_D"));
               personne.setCategorie_e(perm.getInt("CATEGORIE_E"));
               personne.setCategorie_f(perm.getInt("CATEGORIE_F"));
           }

            String jsoninfraction = jsonObject.getString("liste_infraction");
            if(testJson(jsoninfraction)){
                JSONArray arrayInfraction = new JSONArray(jsoninfraction);
                List<Infraction> t = new ArrayList<Infraction>();
                for(int i=0; i<arrayInfraction.length();i++){
                    Infraction inf = new Infraction();
                    JSONObject infjson = arrayInfraction.getJSONObject(i);
                    inf.setId_infraction(infjson.getInt("ID_INFRACTION"));
                    inf.setId_personne(infjson.getInt("ID_PERSONNE"));
                    inf.setId_utilisateur(infjson.getInt("ID_UTILISATEUR"));
                    inf.setDate_infraction(infjson.getString("DATE_INFRACTION"));
                    inf.setRemarque(infjson.getString("REMARQUE"));
                    inf.setDetention(infjson.getInt("DETENTION"));
                    inf.setLibelle(infjson.getString("LIBELLE"));
                    inf.setType(infjson.getString("TYPE"));
                    inf.setAmende(infjson.getDouble("AMENDE"));
                    inf.setEtat(infjson.getInt("ETAT"));
                    t.add(inf);
                }
                personne.setInfractions(t);
            }
            int avisrecherche = jsonObject.getInt("avis_recherche");
            personne.setAvis_recherche(avisrecherche);
            return personne;

    }
    public static boolean testJson(String json){
        json = json.trim();
        if(json!= null &&  !json.isEmpty() && json != "" && json != "[]" && !json.contains("[]")) {
            return true;
        }
        return false;
    }

    public static List<TypeInfraction> getAllTypeInfraction() throws Exception{
        List<TypeInfraction> retour = new ArrayList<TypeInfraction>();
        String jsonresult = getRequest(SERVEUR+"nfcControler/getTypeInfraction/");
        if(testJson(jsonresult)) {
            JSONArray arrayTypeInfraction = new JSONArray(jsonresult);
            for(int i=0; i<arrayTypeInfraction.length();i++){
                TypeInfraction var = new TypeInfraction();
                JSONObject typejson = arrayTypeInfraction.getJSONObject(i);
                var.setId_type_infraction(typejson.getInt("ID_TYPE_INFRACTION"));
                var.setLibelle(typejson.getString("LIBELLE"));
                var.setType(typejson.getString("TYPE"));
                var.setAmende(typejson.getDouble("AMENDE"));
                retour.add(var);
            }
            return retour;
        }
        return null;

    }
    public static List<Infraction> getInfractionByUtilitsateur(String idutilisateur) throws Exception{
        List<Infraction> retour = new ArrayList<>();
        String jsonresult = getRequest(SERVEUR+"nfcControler/infractionByIdUtilisateur/"+idutilisateur);
        if(testJson(jsonresult)) {
            JSONArray arrayTypeInfraction = new JSONArray(jsonresult);
            for(int i=0; i<arrayTypeInfraction.length();i++){
                Infraction var = new Infraction();
                JSONObject typejson = arrayTypeInfraction.getJSONObject(i);
                var.setId_infraction(typejson.getInt("ID_INFRACTION"));
                var.setLibelle(typejson.getString("LIBELLE"));
                var.setDate_infraction(typejson.getString("DATE_INFRACTION"));
                var.setRemarque(typejson.getString("REMARQUE"));
                retour.add(var);
            }
            return retour;
        }
        return retour;

    }
    public static boolean insertInfraction(Infraction inf) throws Exception {
        InputStream is = null;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        Date date = new Date();
        String currentDate= new SimpleDateFormat("yyyy-MM-dd").format(date);
        if(inf.getId_personne() == 0){
            throw new Exception("Personne vide");
        }
        nameValuePairs.add(new BasicNameValuePair("id_personne", ""+inf.getId_personne()));
        nameValuePairs.add(new BasicNameValuePair("id_type_infraction", ""+inf.getId_type()));
        nameValuePairs.add(new BasicNameValuePair("id_utilisateur", ""+inf.getId_utilisateur()));
        nameValuePairs.add(new BasicNameValuePair("date_infraction", currentDate));
        nameValuePairs.add(new BasicNameValuePair("remarque", inf.getRemarque()));
        nameValuePairs.add(new BasicNameValuePair("detention", ""+inf.getDetention()));
        String result = null;
        HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(SERVEUR+"nfcControler/insertInfraction/");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            is = entity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();

            if(result != null && !result.isEmpty() && result.trim().equals("true")){
                return true;
            }

        return false;
    }

    public static String getSeparator(){
        return separator;
    }

    public static String formatterDateString(String aformater){
        String year = aformater.substring(0,4);
        String month = aformater.substring(5,7);
        String day = aformater.substring(8);
        return day + "/"+month+"/"+year;
    }
    public static String getServeur() {return SERVEUR;}

    public static ArrayList<String> rechercheMulticritere(Personne criteria) throws Exception{
        ArrayList<String> retour = new ArrayList<String>();
        InputStream is = null;
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        String nom = criteria.getNom();
        String prenom = criteria.getPrenom();
        String sexe = criteria.getSexecritere();
        String tag = criteria.getNumero_tag();
        String cin = criteria.getImmatriculation();
        nameValuePairs.add(new BasicNameValuePair("nom", nom));
        nameValuePairs.add(new BasicNameValuePair("prenom", prenom));
        nameValuePairs.add(new BasicNameValuePair("date_naiss_max",""));
        nameValuePairs.add(new BasicNameValuePair("date_naiss_min", ""));
        nameValuePairs.add(new BasicNameValuePair("numero_tag", tag));
        nameValuePairs.add(new BasicNameValuePair("immatriculation",cin));
        nameValuePairs.add(new BasicNameValuePair("sexe", ""+sexe));
        String result = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(SERVEUR+"nfcControler/findPersonneMultiple/");
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        HttpResponse response = httpClient.execute(httpPost);

        HttpEntity entity = response.getEntity();

        is = entity.getContent();

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line + "\n");
        }
        result = sb.toString();

        if(result == null || result.isEmpty() || result.trim().equals("Aucune personne trouve")){
            return null;
        }
        JSONArray arrayListePersonne = new JSONArray(result);
        for(int i=0; i<arrayListePersonne.length();i++){
            JSONObject tempjson = arrayListePersonne.getJSONObject(i);
            String temp = tempjson.getInt("ID_PERSONNE") + getSeparator() + tempjson.getString("NOM") + getSeparator() + tempjson.getString("PRENOM") + getSeparator() + tempjson.getString("NUMERO_TAG");
            retour.add(temp);
        }
        return retour;

    }

    public static List<Personne> decoderStringPersonne(ArrayList<String> p){
        List<Personne> retour = new ArrayList<Personne>();
        for(int i =0; i < p.size(); i++){
            String[] persString = p.get(i).split(getSeparator());
            Personne pers = new Personne();
            pers.setId_personne(Integer.valueOf(persString[0]));
            pers.setNom(persString[1]);
            pers.setPrenom(persString[2]);
            pers.setNumero_tag(persString[3]);
            retour.add(pers);

        }
        return retour;
    }

}
