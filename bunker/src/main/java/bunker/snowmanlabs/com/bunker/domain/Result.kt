package bunker.snowmanlabs.com.bunker.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Result(
      @SerializedName("data-nascimento")
      val dataNascimento: String = "",
      @SerializedName("rg")
      val rg: String = "",
      @SerializedName("nome-mae")
      val nomeMae: String = "",
      @SerializedName("categoria")
      val categoria: String = "",
      @SerializedName("cpf")
      val cpf: String = "",
      @SerializedName("nome")
      val nome: String = "",
      @SerializedName("nome-pai")
      val nomePai: String = "",
      @SerializedName("primeira-habilitacao")
      val primeiraHabilitacao: String = "",
      @SerializedName("registro")
      val registro: String = "",
      @SerializedName("validade")
      val validade: String = ""
): Serializable