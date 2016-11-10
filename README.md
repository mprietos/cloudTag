CloudTags
===================


Proyecto que crea una nube de tags en Android. Esta nube es totalmente personalizada, pudiendo controlar su comportamiento como el tamaño, color y si se permite borrar un tag o no.


Configuración
-----

Para añadir la nube basta poner este código en el layout.

    <net.opentrends.cloudtaglibrary.CloudTag
            xmlns:cloudt="http://schemas.android.com/apk/res-auto"
            android:id="@+id/cloudTags"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:padding="10dp"
            android:background="#ffffff"
            cloudt:tagBackGround="#37e1bc"
            cloudt:tagFontSize="7sp"
            cloudt:numberOfTags="0"
            cloudt:maxLines="2"
            cloudt:canDelete="false"
            cloudt:roundedCorners="true"
            cloudt:tagFontColor="#FFFFFF" />

Los parámetros de configuración son los siguientes:

 - tagBackGround: Color de fondo del tag
 - tagFontColor: Color de la fuente del tag
 - tagFontSize: Tamaño de la letra del tag
 - canDelete: Define si se puede borrar el tag o no (true / false)
 - roundedCorners: Define si se queremos que el tag se muestre con los bordes redondeados (true / false)
 - deleteFontColor: Color del simbolo de borrar el tag (si canDelete = true)
 - deleteFontSize: Tamaño del simbolo de borrar el tag (si canDelete = true)
 - numberOfTags: Número máximo de tags que queremos que se muestres (0 se muestran todos)
 - maxLines: Si queremos que se muestre colapsado o no. Si el valor es 0 no se hace nada y se muestran todos los tags. Si pones un valor se muestran esas líneas y da la opción de colapsar / descolpasar


Configuración
-----

Para usar en una activity.
Se define

    mCloudTags = (CloudTag)findViewById(R.id.cloudTags);

Si queremos poder borrar un tag

    mCloudTags.setOnTagDeleteListener(new CloudTag.OnTagDeleteListener() {
                @Override
                public void onTagDeleted(Tag tag, int position) {
                    // Action to do if you want delete a TAG
                }
            });

Para añadir Tags

    for (int i=0;i<20;i++){
		Tag tag = new Tag(i, "Number tag " + i);
        mCloudTags.add(tag);
	}

Para dibujar la nube

    mCloudTags.drawTags();

# cloudTag
