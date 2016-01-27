package polyglot.core;

import java.io.FileNotFoundException;

public interface Mensagens {

	@Messages({
		@Message(locale = Idiomas.PT_BR, value = "Arquivo \"{0}\" não encontrado!"),
		@Message(locale = Idiomas.EN_US, value = "File \"{0}\" not found!")
	})
	public FileNotFoundException arquivoNaoEncontrado(String nomeArquivo);
	
	@Messages({
		@Message(locale = Idiomas.PT_BR, value = "O e-mail de ativação foi enviado com sucesso para \"{enderecoEmailAtivacao}\"!"),
		@Message(locale = Idiomas.EN_US, value = "The activation mail has been sent to \"{enderecoEmailAtivacao}\"!")
	})
	public TemplateBuilder emailAtivacaoEnviadoComSucessoPara();
	
	
	@Messages({
		@Message(locale = Idiomas.PT_BR, value = "Usuário inativo"),
		@Message(locale = Idiomas.EN_US, value = "Inactive user")
	})
	public String usuarioInativo();
	
}
