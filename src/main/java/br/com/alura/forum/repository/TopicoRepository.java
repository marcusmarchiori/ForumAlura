package br.com.alura.forum.repository;

import br.com.alura.forum.modelo.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> { /* Passa qual a classe usada e o tipo do id */

    Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao); /* Só declarar a assinatura do método, o SpringData gera a consulta
                                                    automaticamete */

}
