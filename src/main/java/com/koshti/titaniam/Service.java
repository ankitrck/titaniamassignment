package com.koshti.titaniam;

import com.koshti.titaniam.PrimaryCluster.PrimaryDbRepository;
import com.koshti.titaniam.SecondaryCluster.SecondaryDbRepository;
import com.koshti.titaniam.models.Entities;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class Service implements IService {

    @Autowired
    private PrimaryDbRepository repository;

    @Autowired
    private SecondaryDbRepository secondaryrepository;

    @Override
    public List<Entities> getAll() {
        return repository.getAll();
    }

    @Override
    public Entities getOne(int id) {
        return repository.getOne(id);
    }

    public int syncall() {
        int status = -1;
        List<Entities> primEntities;
        //Force SYNC all Secondary nodes to PRIMARY
        System.out.println("OUTPUT " + secondaryrepository.truncateALL());
        int datareturn = secondaryrepository.truncateALL();
        if(datareturn>=0)
        {
            primEntities = repository.getAll();

            try {
                for (int index = 0; index < primEntities.size(); index++) {
                    secondaryrepository.addAll(primEntities.get(index).getId(), primEntities.get(index).getName());
                }
            }
            catch (Exception ex)
            {
                status = -1;
                ex.printStackTrace();
            }
            status = 1;
        }
        return  status;
    }
}
