package com.barraca.backend.config;

import com.barraca.backend.domain.TipoBarraca;
import com.barraca.backend.repository.TipoBarracaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbLoader implements CommandLineRunner {

    private final TipoBarracaRepository repository;

    public DbLoader(TipoBarracaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            repository.save(new TipoBarraca(null, "Alimentação", "Barracas que vendem refeições, salgados e doces"));
            repository.save(new TipoBarraca(null, "Artesanato", "Barracas que vendem produtos artesanais e decorações"));
            repository.save(new TipoBarraca(null, "Vestuário", "Barracas de roupas, calçados e acessórios"));
        }
    }
}
