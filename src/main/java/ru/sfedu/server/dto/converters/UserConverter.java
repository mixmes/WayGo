package ru.sfedu.server.dto.converters;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sfedu.server.dto.user.UserDTO;
import ru.sfedu.server.model.user.User;

@Component
public class UserConverter implements Converter<User, UserDTO>{
    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    private void initTypeMaps(){
        TypeMap<User,UserDTO> propertyMapper = mapper.createTypeMap(User.class, UserDTO.class);
        propertyMapper.addMappings(mapper -> mapper.map(User::getFavouritePoints, UserDTO::setFavouritePoints));
        propertyMapper.addMappings(mapper -> mapper.map(User::getFavouriteRoutes, UserDTO::setFavouriteRoutes));
        propertyMapper.addMappings(mapper -> mapper.map(User::getPointCheckIns, UserDTO::setPointCheckIns));
        propertyMapper.addMappings(mapper -> mapper.map(User::getRouteCheckIns, UserDTO::setRouteCheckIns));
        propertyMapper.addMappings(mapper -> mapper.map(User::getRouteGrades, UserDTO::setRouteGrades));
//        propertyMapper.addMappings(mapper -> mapper.map(User::getSubscriptionTransactions,UserDTO::setSubscriptionTransactions));
//        propertyMapper.addMappings(mapper -> mapper.map(User::getSubscription, UserDTO::setSubscription));
    }

    @Override
    public UserDTO convertToDto(User entity) {
        return mapper.map(entity, UserDTO.class);
    }

    @Override
    public User convertToEntity(UserDTO dto) {
        return null;
    }
}
