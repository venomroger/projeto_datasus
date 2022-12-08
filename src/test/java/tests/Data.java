package tests;

public class Data {
    private String uf;
    private String municipio;
    private String cnes;
    private String nomefantasia;
    private String natureza;
    private String gestao;
    private String atendesus;

    public Data(String uf, String municipio, String cnes, String nomefantasia, String natureza, String gestao, String atendesus) {
        this.uf = uf;
        this.municipio = municipio;
        this.cnes = cnes;
        this.nomefantasia = nomefantasia;
        this.natureza = natureza;
        this.gestao = gestao;
        this.atendesus = atendesus;

    }

    public String getUf() {
        return uf;
    }
    public String getMunicipio() {
        return municipio;
    }
    public String getCnes() {
        return cnes;
    }
    public String getNomefantasia() {
        return nomefantasia;
    }
    public String getNatureza() {
        return natureza;
    }
    public String getGestao() {
        return gestao;
    }
    public String getAtendesus() {
        return atendesus;
    }
}
