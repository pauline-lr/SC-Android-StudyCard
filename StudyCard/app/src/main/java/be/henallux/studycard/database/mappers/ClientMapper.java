package be.henallux.studycard.database.mappers;

import be.henallux.studycard.models.Client;
import be.henallux.studycard.repositories.web.dto.ClientDto;

public class ClientMapper {
    private static ClientMapper instance = null;

    public static ClientMapper getInstance() {
        if (instance == null) {
            instance = new ClientMapper();
        }
        return instance;
    }

    public Client mapToClient(ClientDto dto) {
        if (dto == null) {
            return null;
        }

        return new Client(dto.getPseudo(), dto.getPassword(), dto.getEmail(),dto.getAdmin());
    }

    public ClientDto mapToClientDto(Client client) {
        if (client == null) {
            return null;
        }

        return new ClientDto(client.id, client.pseudo, client.password, client.email, client.isAdmin);
    }
}

