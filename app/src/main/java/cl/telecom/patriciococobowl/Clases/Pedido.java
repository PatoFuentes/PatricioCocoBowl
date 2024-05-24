package cl.telecom.patriciococobowl.Clases;

public class Pedido {
String direccion, nombre, producto1, producto2, producto3, producto4, producto5, telefono;
public Pedido (){}

    public Pedido(String direccion, String nombre, String producto1, String producto2, String producto3, String producto4, String producto5, String telefono) {
        this.direccion = direccion;
        this.nombre = nombre;
        this.producto1 = producto1;
        this.producto2 = producto2;
        this.producto3 = producto3;
        this.producto4 = producto4;
        this.producto5 = producto5;
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProducto1() {
        return producto1;
    }

    public void setProducto1(String producto1) {
        this.producto1 = producto1;
    }

    public String getProducto2() {
        return producto2;
    }

    public void setProducto2(String producto2) {
        this.producto2 = producto2;
    }

    public String getProducto3() {
        return producto3;
    }

    public void setProducto3(String producto3) {
        this.producto3 = producto3;
    }

    public String getProducto4() {
        return producto4;
    }

    public void setProducto4(String producto4) {
        this.producto4 = producto4;
    }

    public String getProducto5() {
        return producto5;
    }

    public void setProducto5(String producto5) {
        this.producto5 = producto5;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
