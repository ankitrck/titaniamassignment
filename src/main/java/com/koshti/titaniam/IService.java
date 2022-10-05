package com.koshti.titaniam;

import com.koshti.titaniam.models.Entities;

import java.util.List;

public interface IService {
    public List<Entities> getAll();

    public Entities getOne(int id);

    public int syncall();
}
