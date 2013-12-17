package com.example.vendedores;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

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
import com.google.gson.Gson;



import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.InputFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import android.widget.Toast;


@SuppressLint("NewApi")
public class Factura extends Activity {

	private static List<Producto> lista_productos;
	private static HashMap<String, Producto> diccionarioProductos;
	private Double monto_factura;
	private ArrayList<ProductoVenta> productos_factura = new ArrayList<ProductoVenta>();

	private EditText last_text_cantidad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_factura);

		LongRunningGetIO thred=new LongRunningGetIO();//llamo un proceso en backgroud para cargar los productos de la empresa
		 AsyncTask<Void, Void, List<Producto>> async=thred.execute();
		try {
			lista_productos = (ArrayList<Producto>)async.get();
			if(lista_productos!=null)diccionarioProductos=new HashMap<String,Producto>();
			for(int i=0; i<lista_productos.size();i++)
			{
				diccionarioProductos.put(lista_productos.get(i).getCodigo(), lista_productos.get(i));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		addRowToTableProductos();
		this.monto_factura = 0.0;
		EditText monto_value = (EditText)(findViewById(R.id.MontoValue));
	    monto_value.setText(this.monto_factura.toString());
	}

		
		private EditText generarEditText() 
		{
			EditText editText=new EditText(getApplicationContext());
			editText.setTextColor(Color.BLACK);
			editText.setBackgroundColor(Color.WHITE);
			editText.setMaxLines(1);
			editText.setEms(3);
			editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(6)});
			editText.addTextChangedListener(new TextWatcher(){ 
			   
				boolean agregar=true;
				@Override
				public void onTextChanged(CharSequence s, int start, int before,int count) {
					// TODO Auto-generated method stub
				}

				@Override
				public void afterTextChanged(Editable s) {
					try {
						ActualizarFilaFactura((TableRow)getCurrentFocus().getParent(), true);
					} catch (Exception e) {}
					TableLayout tbl=(TableLayout)findViewById(R.id.tablaProductos);
					if(tbl.getChildCount()> 0)
					{
						if(last_text_cantidad.getParent()==tbl.getChildAt(tbl.getChildCount()-1))
						{
							if(agregar)
							{
								addRowToTableProductos();
								agregar=false;
							}	
						};
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					try {
						ActualizarFilaFactura((TableRow)getCurrentFocus().getParent(), false);
					} catch (Exception e) {}
				}
			});
			last_text_cantidad=editText;
			return editText;
		}
		
		
		private void addRowToTableProductos()
		{
			 AutoCompleteTextView autocomplete=Factura.this.generarAutocomplete();
		     EditText text_cant =Factura.this.generarEditText();
		     TableRow tbr= new TableRow(getApplicationContext());
		     EditText divider = Factura.this.generarEditText(); 
		     divider.setBackgroundColor(Color.BLACK);
		     divider.setWidth(2);
		     divider.setInputType(android.text.InputType.TYPE_NULL);
		     Button cruz = Factura.this.generarCruz();
		     tbr.addView(text_cant, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		     tbr.addView(divider);
		     tbr.addView(autocomplete);
		     tbr.addView(cruz);
		     tbr.setLayoutParams(new ViewGroup.LayoutParams(
		                 ViewGroup.LayoutParams.WRAP_CONTENT,
		                 ViewGroup.LayoutParams.WRAP_CONTENT));
		     tbr.setBackgroundColor(Color.LTGRAY);
		     tbr.setPadding(2, 1, 2, 1);
		     TableLayout tbl=(TableLayout)findViewById(R.id.tablaProductos);
		     tbl.addView(tbr,tbl.getChildCount());	
		}
		
		
		private AutoCompleteTextView generarAutocomplete()
		 {
			  // Toast.makeText(Factura.this," selected", Toast.LENGTH_LONG).show();
		     AutoCompleteTextView auto_gen = new AutoCompleteTextView(getApplicationContext());
		     auto_gen.setAdapter(new ArrayAdapter<Producto>(this, android.R.layout.simple_list_item_1, lista_productos));
		     auto_gen.setEms(9);
		     auto_gen.setMaxLines(1);
		     auto_gen.setHighlightColor(Color.BLUE);
		     auto_gen.setHorizontallyScrolling(true);
		     auto_gen.setCursorVisible(true);
		     auto_gen.setTextColor(Color.BLACK);
		     auto_gen.setBackgroundColor(Color.WHITE);
		     auto_gen.addTextChangedListener(new TextWatcher(){ 
				   
					@Override
					public void onTextChanged(CharSequence s, int start, int before,int count) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						try {
							ActualizarFilaFactura((TableRow)getCurrentFocus().getParent(), true);
						} catch (Exception e) {}
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						try {
							ActualizarFilaFactura((TableRow)getCurrentFocus().getParent(), false);
						} catch (Exception e) {}
					}
				});
		     return auto_gen;

		 }
		

		private Button generarCruz()
		 {
		  Button cruz = new Button(getApplicationContext());
		  cruz.setText("X");
		  cruz.setOnClickListener(new OnClickListener() {
		   
		   @Override
		   public void onClick(View v) {
		    TableLayout tbl=(TableLayout)findViewById(R.id.tablaProductos);
		    if (tbl.getChildCount() > 1){
		    	tbl.removeView((View)v.getParent());
		    }
		   }
		  });
		  return cruz;
		 }

    

	@Override
	protected void onResume()
	{
		 super.onResume();
		 
		 	 
	}
		
	private void ActualizarFilaFactura(TableRow row, Boolean sumar) {
		try{
			AutoCompleteTextView auto=(AutoCompleteTextView)row.getChildAt(2);
			EditText cant=(EditText)row.getChildAt(0);
			String codigo="";
			codigo=auto.getText().toString().split("\\s - \\s")[1];	
			if(sumar) {
				this.monto_factura=this.monto_factura+(diccionarioProductos.get(codigo)).getPrecio()*Integer.parseInt(cant.getText().toString());
			}else {
				this.monto_factura=this.monto_factura-(diccionarioProductos.get(codigo)).getPrecio()*Integer.parseInt(cant.getText().toString());
			}
			EditText monto_value = (EditText)(findViewById(R.id.MontoValue));
		    monto_value.setText(this.monto_factura.toString());
		}catch (Exception e) {}
	}
	
	public void Facturar(View view) throws JSONException {
		
		Button b = (Button)findViewById(R.id.Facturar);
		b.setClickable(false);
	    Usuario vendedor=(Usuario)getIntent().getExtras().getParcelable("usuario");
	    Cliente cliente=(Cliente)getIntent().getExtras().getParcelable("cliente");
	    Calendar fecha=Calendar.getInstance();
	    TableLayout tabla = (TableLayout)findViewById(R.id.tablaProductos);
	    for (int i=0; i < tabla.getChildCount();i++) {
	    	try{
	    		TableRow row = ((TableRow)tabla.getChildAt(i));
				AutoCompleteTextView auto=(AutoCompleteTextView)row.getChildAt(2);
				EditText cant=(EditText)row.getChildAt(0);
				String codigo="";
				codigo=auto.getText().toString().split("\\s - \\s")[1];	
				ProductoVenta nueva_linea =new ProductoVenta(diccionarioProductos.get(codigo),Integer.parseInt(cant.getText().toString())); 
				this.productos_factura.add(nueva_linea);
			}catch (Exception e) {}
	    }
	    Venta nueva_venta=new Venta(vendedor, cliente, fecha, this.monto_factura);
	    nueva_venta.setProductos(this.productos_factura);
		JSONObject json=new JSONObject();
	    Gson gson = new Gson();
        	
        do
        {
			String dataString = gson.toJson(nueva_venta, nueva_venta.getClass()).toString(); //venta tentativa espera confirmacion
        	PostNuevaVenta thred=new PostNuevaVenta();//llamo un proceso en backgroud para realizar la venta
        	
        	//inicia el proceso de vender
	        AsyncTask<String, Void, String> async=thred.execute(dataString);	     
			try {				
				//obtengo la respuesta asincrona
				String respuesta= (String)async.get();
				json=new JSONObject(respuesta);

				if (json.getString("response").toString().equalsIgnoreCase("Error en la venta")) {
					
				}else{
					
					//si se creo la venta 
					if(json.getString("response").toString().equalsIgnoreCase("Venta creada"))
					{
						Calendar fecha_venta_registrada=Calendar.getInstance();
						fecha_venta_registrada.set(Calendar.YEAR,Integer.parseInt(((JSONObject)json.get("fecha")).getString("year")));
						fecha_venta_registrada.set(Calendar.MONTH,Integer.parseInt(((JSONObject)json.get("fecha")).getString("month")));
						fecha_venta_registrada.set(Calendar.DAY_OF_MONTH,Integer.parseInt(((JSONObject)json.get("fecha")).getString("dayOfMonth")));
						fecha_venta_registrada.set(Calendar.HOUR_OF_DAY,Integer.parseInt(((JSONObject)json.get("fecha")).getString("hourOfDay")));
						fecha_venta_registrada.set(Calendar.MINUTE,Integer.parseInt(((JSONObject)json.get("fecha")).getString("minute")));
						fecha_venta_registrada.set(Calendar.SECOND,Integer.parseInt(((JSONObject)json.get("fecha")).getString("second")));
						
						
						//JSONObject cliente_venta_creada=((JSONObject)(json.get("cliente")));
						
						// la venta es igual a la que envie originalmente, entonces proceso completo!!!! corto el loop
						if(nueva_venta.getMonto()==Double.parseDouble(json.getString("monto").toString())&&
								nueva_venta.getCliente().getRut().equalsIgnoreCase(json.getString("rut_cliente"))&&
								nueva_venta.getCliente().getNombre().equalsIgnoreCase(json.getString("nombre_cliente"))&&
								nueva_venta.getFecha().equals(fecha_venta_registrada)){
							
							String mensaje="Venta exitosa!"+" Para el cliente "+ nueva_venta.getCliente().getNombre() + "  Con un monto de: "+nueva_venta.getMonto().toString();
							Toast.makeText(Factura.this,mensaje, Toast.LENGTH_LONG).show();
							//break;
						}
						else
						{
							//llamo a crear venta tentativa con nueva_venta
							dataString = gson.toJson(nueva_venta, nueva_venta.getClass()).toString();
							
							JSONObject aux = new JSONObject(dataString); //venta tentativa espera confirmacion
							aux.put("venta_id", json.getString("venta_id"));
							aux.put("monto",json.getString("monto"));
	        				PostNuevaVentaTentativa thred_venta_tentativa=new PostNuevaVentaTentativa();//llamo un proceso en backgroud para realizar la venta
	        	
	        				//inicia el proceso de vender
		        			AsyncTask<String, Void, String> th_async_tentativa=thred_venta_tentativa.execute(aux.toString());	     
							String mensaje=(String)th_async_tentativa.get();
							Toast.makeText(Factura.this,mensaje, Toast.LENGTH_LONG).show();
							//break;
						}
						
					}
					JSONObject ventaObj=((JSONObject)json.get("venta"));
					Double mnt=Double.parseDouble(ventaObj.get("monto").toString());
					//sino llamo nuevamente al proceso de vender con nueva_venta arreglada
					nueva_venta.setMonto(mnt);
					ArrayList<ProductoVenta> nuevaListaProductos=new ArrayList<ProductoVenta>();
					JSONArray productos_nueva_venta=(JSONArray)ventaObj.get("productos");				
					for(int i=0;i<productos_nueva_venta.length();i++)
					{
						String codigo =((String)((JSONObject)productos_nueva_venta.get(i)).get("producto")).split(":")[2];
						Producto p = diccionarioProductos.get(codigo);	
						int cant=Integer.parseInt(((JSONObject)productos_nueva_venta.get(i)).get("cantidad").toString());
						ProductoVenta pv=new ProductoVenta(p,cant);
						nuevaListaProductos.add(pv);
					}
					
					nueva_venta.setProductos(nuevaListaProductos);
				}
			}catch(Exception e)	{}
			
        }while(json.getString("response").toString().equalsIgnoreCase("Stock insuficiente"));
					
				
         
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.factura, menu);
		
		return true;
	}
	
private class LongRunningGetIO extends AsyncTask <Void, Void, List<Producto> > {
		
		 
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
		protected  List<Producto> doInBackground(Void... params) {
			ArrayList<Producto> lista = null;
			HttpClient httpClient = new DefaultHttpClient();
			 HttpContext localContext = new BasicHttpContext();
             HttpGet httpGet = new HttpGet("http://ventas.jm-ga.com/api/productos/"+((Usuario)getIntent().getExtras().getParcelable("usuario")).getKey()+"/");
           
  
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
 				
 				
 				JSONObject jsonObject;
 				try {
 					jsonObject = new JSONObject(text);
 										
 					JSONArray jarray =(JSONArray)jsonObject.get("objects");
 					lista= new ArrayList<Producto>();
 					for(int i=0;i<jarray.length();i++)
 					{
 						JSONObject dic_producto = jarray.getJSONObject(i);
 						Producto prod= new Producto(dic_producto.getString("nombre"),dic_producto.getDouble("precio"),dic_producto.getString("codigo"),dic_producto.getString("descripcion"));
 						lista.add(prod);
 					}
 					
 					
 				} catch (JSONException e) {
 					e.printStackTrace();
 				}
 					
 					
 			}
			return lista;
	}


	}	
    
private class PostNuevaVenta extends AsyncTask <String, Void, String > {
		
	 
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
		protected  String doInBackground(String... params) {
			 
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost("http://ventas.jm-ga.com/api/ventas/concreta/");
             // Execute HTTP Post Request
             String text = null;
             try {
            	 StringEntity se = new StringEntity(params[0].toString());
            	 se.setContentEncoding("UTF-8");
            	 se.setContentType("application/json");
            	 httpPost.setEntity(se);
            	 HttpResponse response = httpClient.execute(httpPost, localContext);
            	 HttpEntity entity = response.getEntity();
                   
                 text = getASCIIContentFromEntity(entity);
                 return text;
                   
             } catch (Exception e) {}
             //return text; 
             
 			
			return null;
	}
		@Override
		protected void onPostExecute(String results) {
			//((ProgressBar)findViewById(R.id.progressBarFactura)).setVisibility(View.INVISIBLE);
		}

	}
	
	
	
	private class PostNuevaVentaTentativa extends AsyncTask <String, Void, String > {
		
	 
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
		protected  String doInBackground(String... params) {
			 
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost("http://ventas.jm-ga.com/api/ventas/tentativa/");
             // Execute HTTP Post Request
             String text = null;
             try {
            	 StringEntity se = new StringEntity(params[0].toString());
            	 se.setContentEncoding("UTF-8");
            	 se.setContentType("application/json");
            	 httpPost.setEntity(se);
            	 HttpResponse response = httpClient.execute(httpPost, localContext);
            	 HttpEntity entity = response.getEntity();
                   
                 text = getASCIIContentFromEntity(entity);
                 return text;
                   
             } catch (Exception e) {}
             //return text; 
             
 			
			return null;
	}
		@Override
		protected void onPostExecute(String results) {
			//((ProgressBar)findViewById(R.id.progressBarFactura)).setVisibility(View.INVISIBLE);
		}

	}
	
}
