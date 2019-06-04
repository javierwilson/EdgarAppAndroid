package org.lwr.edgar.adapter

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import org.lwr.edgar.Constants
import org.lwr.edgar.Documentos
import org.lwr.edgar.R
import org.lwr.edgar.common.DescargarArchivos
import org.lwr.edgar.database.ArchivosDB
import org.lwr.edgar.models.documentosM
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


class AdapterDocumentos(private val mContext: Activity, private val listCalendario: List<documentosM>) : RecyclerView.Adapter<AdapterDocumentos.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txTitulo: TextView? = null
        var tipo: ImageView?=null
        var eye: ImageView?=null

        init {
            txTitulo = view.findViewById(R.id.txTitulo) as TextView
            tipo = view.findViewById(R.id.download) as ImageView
            eye = view.findViewById(R.id.eye) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_documentos, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val calendarioItem = listCalendario[position]

        holder.txTitulo!!.setText(calendarioItem.titulo)


        if(calendarioItem.descargado==true){
            Picasso.get()
                    .load(R.drawable.refresh_button)
                    .into(holder.tipo)

            holder.eye!!.visibility = View.VISIBLE

        }else{
            Picasso.get()
                    .load(R.drawable.download)
                    .into(holder.tipo)

            holder.eye!!.visibility = View.GONE
        }


        holder.tipo!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                var mensaje = "Estas a punto de descargar este archivo para poder compartirla con tus amigos, esto puede ocupar espacio en tu celular\n" +
                        "\n" +
                        "¿Deseas continuar?"
                if(calendarioItem.descargado==true){
                    mensaje = "Estas a punto de actualizar este archivo para poder compartirla con tus amigos, esto puede ocupar espacio en tu celular\n" +
                            "\n" +
                            "¿Deseas continuar?"
                }

                //val perms = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

                val perms = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

                if(!EasyPermissions.hasPermissions(mContext, *perms)){
                    ActivityCompat.requestPermissions(mContext, perms, 42);
                }else{
                    val builder: AlertDialog.Builder
                    builder = AlertDialog.Builder(mContext)
                    builder.setTitle("Edgar App")
                            .setMessage(mensaje)
                            .setPositiveButton("Si", DialogInterface.OnClickListener { dialog, which ->
                                val dialog = ProgressDialog.show(mContext, "","Cargando. Por favor espera...", true)

                                val nuevaCarpeta = File(Environment.getExternalStorageDirectory(), Constants.nameFolder)
                                nuevaCarpeta.mkdirs()


                                if(calendarioItem.descargado==true){
                                    var urlArchivo = Documentos.ArchivosData.getArchivoByArchivo(calendarioItem.id)
                                    var nombreArchivo = ""
                                    if (urlArchivo.moveToFirst())
                                        do {
                                            nombreArchivo = urlArchivo.getString(urlArchivo.getColumnIndex(ArchivosDB.name))
                                        } while (urlArchivo.moveToNext())

                                    var file = File("/storage/emulated/0/"+Constants.nameFolder+"/"+nombreArchivo)
                                    if(file.exists()){
                                        file.delete()
                                    }
                                }


                                var urlArchivo = calendarioItem.file
                                var file_name = calendarioItem.titulo.trim()
                                var file_type= "PDF"
                                dialog.dismiss()
                                DescargarArchivos(urlArchivo,file_name,file_type,calendarioItem.id,position,mContext)


                            })
                            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                                // do nothing
                            })
                            .setIcon(R.mipmap.ic_launcher)
                            .show()

                }


            }
        })


        holder.eye!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                val obtenerRegistro = Documentos.ArchivosData.getArchivoByArchivo(calendarioItem.id)
                var nombreArchivo = ""

                if(obtenerRegistro.count>0){
                    if (obtenerRegistro.moveToFirst())
                        do {
                            nombreArchivo = obtenerRegistro.getString(obtenerRegistro.getColumnIndex(ArchivosDB.name))
                        } while (obtenerRegistro.moveToNext())
                }

                if (!nombreArchivo.toString().equals("")){
                    var nuevaCarpeta = File(Environment.getExternalStorageDirectory(), Constants.nameFolder)
                    nuevaCarpeta.mkdirs();

                    var file = File(nuevaCarpeta, nombreArchivo)
                    if (file.exists()) {
                        var path = Uri.fromFile(file)
                        //var path = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".com.cacaomovil.app.provider", file)

                        try {
                            var intent = Intent(Intent.ACTION_VIEW)

                            if(calendarioItem.formato.toLowerCase().toString().equals("pdf")){
                                intent.setDataAndType(path, "application/pdf")
                            }else if(calendarioItem.formato.toLowerCase().toString().equals("")){
                                intent.setDataAndType(path, "application/epub+zip")
                            }
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            mContext.startActivity(intent)
                            //finish();
                        } catch (ee: ActivityNotFoundException) {
                            if(calendarioItem.formato.toLowerCase().toString().equals("pdf")){
                                var toast1 = Toast.makeText(Documentos.context,"La aplicación PDF Reader no está instalada en su dispositivo", Toast.LENGTH_SHORT);
                                toast1.show()
                            }else if(calendarioItem.formato.toLowerCase().toString().equals("")){
                                var toast1 = Toast.makeText(Documentos.context,"Necesitas instalar una aplicacion para leer archivo Epub", Toast.LENGTH_SHORT);
                                toast1.show()
                            }

                        }
                    } else {
                        var toast1 = Toast.makeText(Documentos.context,"Lo sentimos, el archivo no existe. Inténtelo descargándolo de nuevo", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                }



            }
        })

        /*holder.poFavorito.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {

                Log.e("valor", "click " + checked);
            }
        });*/
        /*holder.nombre.setText(album.getNombre());
        holder.precio.setText("$"+album.getPrecio());
        holder.descripcion.setText(album.getDescripcion());*/

    }

    override fun getItemCount(): Int {
        return listCalendario.size
    }
}
