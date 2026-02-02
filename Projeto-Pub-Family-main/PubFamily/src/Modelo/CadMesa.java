
package Modelo;


public class CadMesa {
    private int ID;
    private String assentos; 
    private int id_usuario;
    private boolean cheia;
    
    
    public CadMesa(){}
    
    public CadMesa(int id, String assentos, int id_usuario){
        this.ID = id;
        this.assentos = assentos;
        this.id_usuario = id_usuario;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAssentos() {
        return assentos;
    }

    public void setAssentos(String assentos) {
        this.assentos = assentos;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    
    public boolean isCheia() {
        return cheia;
    }

   
    public void setCheia(boolean cheia) {
        this.cheia = cheia;
    }
    
}
