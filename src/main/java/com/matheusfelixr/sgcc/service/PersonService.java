package com.matheusfelixr.sgcc.service;

import com.matheusfelixr.sgcc.model.domain.Person;
import com.matheusfelixr.sgcc.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    public Optional<List<Person>> findByExample(Person person) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll();
        Example<Person> example = Example.<Person>of(person, matcher);

        return Optional.ofNullable(personRepository.findAll(example));
    }
}
