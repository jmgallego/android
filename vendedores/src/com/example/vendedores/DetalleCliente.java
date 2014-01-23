package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dominio.Cliente;
import com.example.dominio.Producto;
import com.example.dominio.ProductoVenta;
import com.example.dominio.Usuario;
import com.example.dominio.Venta;
import com.google.android.gms.internal.bj;



import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleCliente extends Activity {
	private String[] dias_de_semana = {"Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"}; 
	private Venta venta;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detalle_cliente);
		Cliente cliente=getIntent().getExtras().getParcelable("cliente");
		TextView nombre=(TextView)findViewById(R.id.editTextNombreCliente);
		TextView rut=(TextView)findViewById(R.id.editTextRutCliente);
		TextView direccion=(TextView)findViewById(R.id.editDireccionCliente);
		TextView tel=(TextView)findViewById(R.id.editTelCliente);
		TextView tel2=(TextView)findViewById(R.id.editTel2Cliente);
		TextView celular=(TextView)findViewById(R.id.editCelularCliente);
		TextView email=(TextView)findViewById(R.id.editEmailCliente);
		TextView web=(TextView)findViewById(R.id.editWebCliente);
		TextView lugar_entrega=(TextView)findViewById(R.id.editLugarEntrega);
		TextView dia_entrega=(TextView)findViewById(R.id.editDia_de_entregaCliente);
		TextView hora_entrega_desde=(TextView)findViewById(R.id.editHora_de_entrega_desde);
		TextView hora_entrega_hasta=(TextView)findViewById(R.id.editHora_de_entrega_hasta);
		
		if(cliente.getNombre()!=null) nombre.setText("Nombre:\n"+cliente.getNombre());
		else nombre.setVisibility(View.GONE);
		if(cliente.getRut()!=null) rut.setText("Rut:\n"+cliente.getRut());
		else rut.setVisibility(View.GONE);
		if(cliente.getDireccion()!=null) direccion.setText("Dir:\n"+cliente.getDireccion());
		else direccion.setVisibility(View.GONE);
		if(cliente.getTel()!=null)tel.setText("Tel:\n"+	cliente.getTel());
		else tel.setVisibility(View.GONE);
		if(cliente.getTel2()!=null)tel2.setText("Tel2:\n"+cliente.getTel2());
		else tel2.setVisibility(View.GONE);
		if(cliente.getCelular()!=null)celular.setText("Celular:\n"+	cliente.getCelular());
		else celular.setVisibility(View.GONE);
		if(cliente.getEmail()!=null)email.setText("Email:\n"+cliente.getEmail());
		else email.setVisibility(View.GONE);
		if(cliente.getWeb()!=null)web.setText("Web:\n"+cliente.getWeb());
		else web.setVisibility(View.GONE);
		if(cliente.getLugar_entrega()!=null)lugar_entrega.setText("Dirección de entrega:\n"+cliente.getLugar_entrega());
		else lugar_entrega.setVisibility(View.GONE);
		if(cliente.getDia_de_entrega()!=null)dia_entrega.setText("Día de entrega:\n"+dias_de_semana[cliente.getDia_de_entrega()]);
		else dia_entrega.setVisibility(View.GONE);
		if(cliente.getHora_de_entrega_desde()!=null)hora_entrega_desde.setText("\nHora de entrega:\n\ndesde: " +cliente.getHora_de_entrega_desde().get(Calendar.HOUR_OF_DAY) + ":" + cliente.getHora_de_entrega_desde().get(Calendar.MINUTE) + "hs.");
		else hora_entrega_desde.setVisibility(View.GONE);
		if(cliente.getHora_de_entrega_hasta()!=null)hora_entrega_hasta.setText(	"hasta: " +	cliente.getHora_de_entrega_hasta().get(Calendar.HOUR_OF_DAY) + ":" + cliente.getHora_de_entrega_hasta().get(Calendar.MINUTE) + "hs.");
	
		
		//ImageView mainImageView = (ImageView) findViewById(R.id.imageView);
		//ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		//String imageurl = "http://ventas.jm-ga.com/source/media/"+cliente.getUrl_imagen();
		  
		
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detalle_cliente, menu);
		return true;
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		((ProgressBar)findViewById(R.id.progressBarDetalleAFactura)).setVisibility(View.INVISIBLE);
	}
	
	public void to_factura_activity(View view){
		((ProgressBar)findViewById(R.id.progressBarDetalleAFactura)).setVisibility(View.VISIBLE);
		Intent fac_intent = new Intent(getApplicationContext(),EleccionFactura.class); 
		fac_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario")); 
		fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
		fac_intent.putExtra("descuento_contado",getIntent().getExtras().getInt("descuento_contado"));
	    startActivity(fac_intent);
	}
	
	public void to_factura_activity_con_venta(View view){
		((ProgressBar)findViewById(R.id.progressBarDetalleAFactura)).setVisibility(View.VISIBLE);
		GetUltimaVenta thred=new GetUltimaVenta();//llamo un proceso en backgroud para realizar la venta
    	
    	//inicia el proceso de cargar la ultima venta
        AsyncTask<Void, Void, Venta> async=thred.execute();	     
        Venta respuesta=null;
        try {				
			//obtengo la respuesta asincrona
			respuesta= (Venta)async.get();
			
		}catch(Exception e){}
		
        if(respuesta!=null)
        {
        	Intent fac_intent = new Intent(getApplicationContext(),Factura.class); 
			fac_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario")); 
			fac_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
			fac_intent.putExtra("venta",respuesta);
		    startActivity(fac_intent);
        }else
	        {
	        	 Toast.makeText(DetalleCliente.this,"No hay ventas asociadas para este cliente",Toast.LENGTH_LONG).show(); 
	        	 ((ProgressBar)findViewById(R.id.progressBarDetalleAFactura)).setVisibility(View.INVISIBLE);
	        }//aca en el else tengo que informar sobre el error al levantar las estadisticas
	}
	
	
public void to_historico_activity(View view){
		
		((ProgressBar)findViewById(R.id.progressBarDetalleAFactura)).setVisibility(View.VISIBLE);
		GetHistorico thread=new GetHistorico();//llamo un proceso en backgroud para realizar la estadistica
    	
    	//inicia el proceso de cargar estadisticas del cliente
        AsyncTask<Void, Void, HashMap<Integer,Double>> async=thread.execute();	     
        HashMap<Integer, Double> respuesta=null;
        try {				
			//obtengo la respuesta asincrona
			respuesta=(HashMap<Integer,Double>)async.get();
			
		}catch(Exception e){}
		
        if(respuesta!=null)
        {
        	Intent hist_intent = new Intent(getApplicationContext(),Historico.class);  
        	hist_intent.putExtra("usuario",getIntent().getExtras().getParcelable("usuario")); 
        	hist_intent.putExtra("cliente",getIntent().getExtras().getParcelable("cliente"));
        	Bundle extras = new Bundle();
        	extras.putSerializable("dict", (Serializable) respuesta);
        	hist_intent.putExtras(extras);
        	startActivity(hist_intent);
        }
        else
        {
        	 Toast.makeText(DetalleCliente.this,"No hay ventas asociadas para este cliente",Toast.LENGTH_LONG).show(); 
        	 ((ProgressBar)findViewById(R.id.progressBarDetalleAFactura)).setVisibility(View.INVISIBLE);
        }//aca en el else tengo que informar sobre el error al levantar las estadisticas
	}
	




	 
	 
	 
private class GetUltimaVenta extends AsyncTask <Void, Void, Venta > {
			
		 
			protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
		       InputStream in = entity.getContent();
		         StringBuffer out = new StringBuffer();
		         int n = 1;
		         while (n>0) {
		             byte[] b = new byte[4096];
		             n =  in.read(b);
		             if (n>0) out.append(new String(b, 0, n));
		         }
		         return out.toString();
		    }
			
			@Override
			protected  Venta doInBackground(Void... params) {
				venta=null;
				ArrayList<ProductoVenta> lista = null;
				HttpClient httpClient = new DefaultHttpClient();
				 HttpContext localContext = new BasicHttpContext();
				 
				 //http://ventas.jm-ga.com/api/ventas/ultima/?nombre=luis&rut=123132&key=39b212ca41d9564d917bf7a9748746d52ffe28c9

		         HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/estadisticas/ventas/ultima/?nombre="+((Usuario)getIntent().getExtras().getParcelable("usuario")).getNombreUsuario()+"&key="+((Usuario)getIntent().getExtras().getParcelable("usuario")).getKey()+"&rut="+((Cliente)getIntent().getExtras().getParcelable("cliente")).getRut());
		           
		  
		             // Execute HTTP Post Request
		         String text = null;
		         try {
		        	 HttpResponse response = httpClient.execute(httpGet, localContext);
		        	 HttpEntity entity = response.getEntity();
		               
		             text = getASCIIContentFromEntity(entity);
		               
		         } catch (Exception e) {
		        	 
		         }
		         //return text; 
		         if (text!=null) {
		        	 if(!text.equals("No hay ventas"))
		        	 {			
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(text);
							
							Calendar fecha_venta_registrada=Calendar.getInstance();
							fecha_venta_registrada.set(Calendar.YEAR,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("year")));
							fecha_venta_registrada.set(Calendar.MONTH,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("month"))-1);
							fecha_venta_registrada.set(Calendar.DAY_OF_MONTH,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("dayOfMonth")));
							fecha_venta_registrada.set(Calendar.HOUR_OF_DAY,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("hourOfDay")));
							fecha_venta_registrada.set(Calendar.MINUTE,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("minute")));
							fecha_venta_registrada.set(Calendar.SECOND,Integer.parseInt(((JSONObject)jsonObject.get("fecha")).getString("second")));
							
							venta=new Venta((Usuario)getIntent().getExtras().getParcelable("usuario"),(Cliente)getIntent().getExtras().getParcelable("cliente"),fecha_venta_registrada,Double.parseDouble(jsonObject.get("precio").toString()),Integer.parseInt(jsonObject.get("tipo").toString()));
							
							JSONArray jarray =(JSONArray)jsonObject.get("productos");
							lista= new ArrayList<ProductoVenta>();
							for(int i=0;i<jarray.length();i++)
							{
								JSONObject jsonPvp=(JSONObject)((JSONObject)jarray.get(i)).get("producto");
								int cant=((JSONObject)jarray.get(i)).getInt("cantidad");
								int desc=((JSONObject)jarray.get(i)).getInt("descuento");
								boolean sin_costo=((JSONObject)jarray.get(i)).getBoolean("sin_cargo");
								int s_costo=1;
								if(sin_costo)
									s_costo=0;
								Producto prod= new Producto(jsonPvp.getString("nombre"),
										new BigDecimal(jsonPvp.getDouble("precio_cliente_final")),
										new BigDecimal(jsonPvp.getDouble("precio_distribuidor")),
										new BigDecimal(jsonPvp.getDouble("precio_mayorista")),
										jsonPvp.getString("codigo"),jsonPvp.getString("descripcion"));
								ProductoVenta pv=new ProductoVenta(prod,cant,desc,s_costo);
								venta.getProductos().add(pv);
							}
						
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
		        	 } 
						
				}
				return venta;
			}


		}



private class GetHistorico extends AsyncTask <Void, Void, HashMap<Integer, Double> > {
	
	 
	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
       InputStream in = entity.getContent();
         StringBuffer out = new StringBuffer();
         int n = 1;
         while (n>0) {
             byte[] b = new byte[4096];
             n =  in.read(b);
             if (n>0) out.append(new String(b, 0, n));
         }
         return out.toString();
    }
	
	@Override
	protected  HashMap<Integer, Double>  doInBackground(Void... params) {
		
		HashMap<Integer, Double> retorno=null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		 
		 //http://ventas.jm-ga.com/api/ventas/ultima/?nombre=luis&rut=123132&key=39b212ca41d9564d917bf7a9748746d52ffe28c9

         HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/estadisticas/ventas/porcentaje_productos_cliente/?rut="+((Cliente)getIntent().getExtras().getParcelable("cliente")).getRut());
           
  
             // Execute HTTP Post Request
         String text = null;
         try {
        	 HttpResponse response = httpClient.execute(httpGet, localContext);
        	 HttpEntity entity = response.getEntity();
               
             text = getASCIIContentFromEntity(entity);
               
         } catch (Exception e) {
        	 
         } 
         if (text!=null) {
        	 if(!text.contains("Error"))
        	 {			
				try {
					retorno = new HashMap<Integer, Double>();
					JSONArray jarray =new JSONArray(text);
					for(int i=0;i<jarray.length();i++)
					{
						JSONArray aux =(JSONArray)jarray.get(i);
						retorno.put(Integer.parseInt(aux.get(0).toString()), Double.parseDouble(aux.get(1).toString()));
						//Toast.makeText(DetalleCliente.this,retorno.toString(), Toast.LENGTH_LONG).show();
					}
				
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	 }else
        	 {
        		 //Toast.makeText(DetalleCliente.this,text, Toast.LENGTH_LONG).show(); 
        	 }
				
		}
		return retorno;
	}


}


	 
}
