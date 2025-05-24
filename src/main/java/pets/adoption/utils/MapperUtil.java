package pets.adoption.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MapperUtil {
    
    private final ModelMapper modelMapper;
    
    public <D> D map(Object source, Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
    }
    
    public void map(Object source, Object destination) {
        modelMapper.map(source, destination);
    }
}