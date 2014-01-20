package com.example.dominio;

import java.util.Calendar;

import android.R.integer;
import android.os.Parcel;
import android.os.Parcelable;

public class Cliente implements Parcelable{

	
	private String nombre;//
	private String direccion;//
	private String lugar_entrega;
	private String rut;//
	private String tel;
	private String tel2;
	private String celular;
	private String email;
	private String web;
	private Integer latitud=0;
	private Integer longitud=0;
	private String url_imagen;
	private Integer dia_de_entrega=0;//
	private Calendar hora_de_entrega_desde=Calendar.getInstance();//
	private Calendar hora_de_entrega_hasta=Calendar.getInstance();//
	private Integer tipo;
	
	
	
	public Cliente(String nombre,String direccion,String rut,String url_imagen,String dia_entrega,String hora_de_entrega_desde,String minuto_de_entrega_desde,
			String hora_de_entrega_hasta,String minuto_de_entrega_hasta,String tel,String tel2,String celular,String email,String web,String lugarEntrega,Integer tipo)
	{
		this.nombre=nombre;//
		this.direccion=direccion;//
		this.rut=rut;//
		if(dia_entrega!=null&&dia_entrega!="")
			this.dia_de_entrega=Integer.parseInt(dia_entrega);
		else this.dia_de_entrega=null;
		
		
		if(!hora_de_entrega_desde.equals("")&&!hora_de_entrega_desde.equals("null")&& hora_de_entrega_desde!=null&&!minuto_de_entrega_desde.equals("")&&!minuto_de_entrega_desde.equals("null")&& minuto_de_entrega_desde!=null)
		{
			this.hora_de_entrega_desde=Calendar.getInstance();
			this.hora_de_entrega_desde.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hora_de_entrega_desde));
			this.hora_de_entrega_desde.set(Calendar.MINUTE,Integer.parseInt(minuto_de_entrega_desde));
		}
		else this.hora_de_entrega_desde=null; 
		
		
		if(!hora_de_entrega_hasta.equals("")&&!hora_de_entrega_hasta.equals("null")&& hora_de_entrega_hasta!=null&&!minuto_de_entrega_hasta.equals("")&&!minuto_de_entrega_hasta.equals("null")&& minuto_de_entrega_hasta!=null)
		{
			this.hora_de_entrega_hasta=Calendar.getInstance();
			this.hora_de_entrega_hasta.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hora_de_entrega_hasta));
			this.hora_de_entrega_hasta.set(Calendar.MINUTE,Integer.parseInt(minuto_de_entrega_hasta));
		}
		else this.hora_de_entrega_hasta=null; 
		
		this.tel=tel;
		this.tel2=tel2;
		this.celular=celular;
		this.web=web;
		this.url_imagen=url_imagen;
		latitud=0;
		longitud=0;
		this.email=email;
		this.lugar_entrega=lugarEntrega;
		this.setTipo(tipo);
		
		
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nombre);
		dest.writeString(direccion);
		dest.writeString(lugar_entrega);
		dest.writeString(rut);
		dest.writeString(tel);
		dest.writeString(tel2);
		dest.writeString(celular);
		dest.writeString(email);
		dest.writeString(web);
		
		/*if(latitud!=null)
			dest.writeString(latitud.toString());
		else
			dest.writeString("");
		
		if(longitud!=null)
			dest.writeString(longitud.toString());
		else
			dest.writeString("");*/
		
		
		dest.writeString(url_imagen);
		
		if(dia_de_entrega!=null)
			dest.writeString(dia_de_entrega.toString());
		else
			dest.writeString("");
		
		if(hora_de_entrega_desde!=null)
			dest.writeString(Long.toString(hora_de_entrega_desde.getTimeInMillis()));
		else
			dest.writeString("");
		
		if(hora_de_entrega_desde!=null)
			dest.writeString(Long.toString(hora_de_entrega_hasta.getTimeInMillis()));
		else
			dest.writeString("");
		
		dest.writeInt(tipo);
		
		
	}
	public static final Parcelable.Creator<Cliente> CREATOR
    = new Parcelable.Creator<Cliente>() {
		 
	 public Cliente createFromParcel(Parcel in) {
	     return new Cliente(in);
	 }
	
	 public Cliente[] newArray(int size) {
	     return new Cliente[size];
	 }
	};

	private Cliente(Parcel in) {
		nombre = in.readString();
		direccion = in.readString();
		lugar_entrega = in.readString();
		rut = in.readString();
		tel = in.readString();
		tel2 = in.readString();
		celular = in.readString();
		email = in.readString();
		web = in.readString();
		
		String aux;
		/*aux=in.readString();
		if(!aux.equals("")&& aux!=null)
			latitud = in.readInt();
		else latitud=null; 
		
		
		aux=in.readString();
		if(!aux.equals("")&& aux!=null)
			longitud = in.readInt();
		else longitud=null; */
		
		url_imagen=in.readString();
		
		aux=in.readString();
		if(!aux.equals("")&& aux!=null)
			dia_de_entrega=Integer.parseInt(aux);
		else dia_de_entrega=null; 
		
		aux=in.readString();
		if(!aux.equals("")&& aux!=null)
		{
			hora_de_entrega_desde=Calendar.getInstance();
			hora_de_entrega_desde.setTimeInMillis(Long.parseLong(aux));
		}
		else hora_de_entrega_desde=null; 
		
		aux=in.readString();
		if(!aux.equals("")&& aux!=null)
		{
			hora_de_entrega_hasta=Calendar.getInstance();
			hora_de_entrega_hasta.setTimeInMillis(Long.parseLong(aux));
		}
		else hora_de_entrega_hasta=null; 
		
		tipo = in.readInt();
	}
	
	@Override
	public String toString(){
		return this.nombre + "  -  " + this.rut; 
	}
	public String getUrl_imagen() {
		return url_imagen;
	}
	public void setUrl_imagen(String url_imagen) {
		this.url_imagen = url_imagen;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getTel2() {
		return tel2;
	}
	public void setTel2(String tel2) {
		this.tel2 = tel2;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	public Integer getLatitud() {
		return latitud;
	}
	public void setLatitud(Integer latitud) {
		this.latitud = latitud;
	}
	public Integer getLongitud() {
		return longitud;
	}
	public void setLongitud(Integer longitud) {
		this.longitud = longitud;
	}
	public String getLugar_entrega() {
		return lugar_entrega;
	}
	public void setLugar_entrega(String lugar_entrega) {
		this.lugar_entrega = lugar_entrega;
	}
	public Integer getDia_de_entrega() {
		return dia_de_entrega;
	}
	public void setDia_de_entrega(Integer dia_de_entrega) {
		this.dia_de_entrega = dia_de_entrega;
	}
	public Calendar getHora_de_entrega_desde() {
		return hora_de_entrega_desde;
	}
	public void setHora_de_entrega_desde(Calendar hora_de_entrega_desde) {
		this.hora_de_entrega_desde = hora_de_entrega_desde;
	}
	public Calendar getHora_de_entrega_hasta() {
		return hora_de_entrega_hasta;
	}
	public void setHora_de_entrega_hasta(Calendar hora_de_entrega_hasta) {
		this.hora_de_entrega_hasta = hora_de_entrega_hasta;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	
}
				
