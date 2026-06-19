package com.reservas.reservas;

import com.reservas.reservas.model.Reserva;
import com.reservas.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final ReservaRepository reservaRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        Random random = new Random();


        for (int i = 0; i < 20; i++) {
            Reserva reserva = new Reserva();
            reserva.setIdUsuario(Long.valueOf( faker.number().numberBetween(1, 50)));
            reserva.setIdEmp(Long.valueOf( faker.number().numberBetween(1, 50)));
            reserva.setTipoCita(faker.options().option(
                    "control",
                    "vacunacion",
                    "consulta",
                    "cirugia"
                    )
            );
            reserva.setFechaCita(
                    LocalDate.now().plusDays(random.nextInt(30)+1)
            );
            reserva.setDisponibilidadCita(
                    faker.options().option(
                            "RESERVADA",
                            "CANCELADA"
                    )
            );
            reservaRepository.save(reserva);

        }
        System.out.println("20 Reservas creadas");

    }
}
