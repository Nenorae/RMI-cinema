package com.cinema.ticketing.config;

import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.cinema.ticketing.rmi.impl.BookingRemoteServiceImpl;
import com.cinema.ticketing.rmi.impl.CinemaRemoteServiceImpl;
import com.cinema.ticketing.rmi.remote.BookingRemoteService;
import com.cinema.ticketing.rmi.remote.CinemaRemoteService;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RmiConfig {

    @Value("${rmi.registry.port:1099}")
    private int registryPort;

    @Value("${rmi.service.port:1098}")
    private int servicePort;

    @Value("${rmi.service.host:localhost}")
    private String serviceHost;

    @Autowired
    private CinemaRemoteServiceImpl cinemaRemoteService;

    @Autowired
    private BookingRemoteServiceImpl bookingRemoteService;

    // Keep track of exported objects for cleanup
    private List<Object> exportedObjects = new ArrayList<>();
    private Registry registry;

    @Bean
    public Registry rmiRegistry() throws RemoteException {
        if (registry != null) {
            return registry;
        }
        
        try {
            // Try to create registry
            registry = LocateRegistry.createRegistry(registryPort);
            log.info("RMI Registry created on port: {}", registryPort);
        } catch (RemoteException e) {
            // If registry already exists, locate it
            registry = LocateRegistry.getRegistry(registryPort);
            log.info("RMI Registry located on port: {}", registryPort);
        }
        return registry;
    }

    @Bean
    @DependsOn("rmiRegistry")
    public CinemaRemoteService cinemaRemoteServiceStub() throws RemoteException, AlreadyBoundException {
        // Set system properties for RMI
        System.setProperty("java.rmi.server.hostname", serviceHost);
        
        // Check if already exported
        if (isObjectExported(cinemaRemoteService)) {
            log.warn("CinemaRemoteService already exported, skipping export");
            return findExistingStub(cinemaRemoteService);
        }
        
        try {
            // Export the remote object
            CinemaRemoteService stub = (CinemaRemoteService) UnicastRemoteObject.exportObject(
                cinemaRemoteService, servicePort);
            
            // Track exported object
            exportedObjects.add(cinemaRemoteService);
            
            // Bind to registry
            Registry reg = rmiRegistry();
            try {
                reg.bind("CinemaService", stub);
                log.info("CinemaRemoteService bound to registry with name: CinemaService");
            } catch (AlreadyBoundException e) {
                reg.rebind("CinemaService", stub);
                log.info("CinemaRemoteService rebound to registry with name: CinemaService");
            }
            
            return stub;
        } catch (RemoteException e) {
            log.error("Failed to export CinemaRemoteService: {}", e.getMessage());
            throw e;
        }
    }

    @Bean
    @DependsOn("rmiRegistry")
    public BookingRemoteService bookingRemoteServiceStub() throws RemoteException, AlreadyBoundException {
        // Check if already exported
        if (isObjectExported(bookingRemoteService)) {
            log.warn("BookingRemoteService already exported, skipping export");
            return findExistingStub(bookingRemoteService);
        }
        
        try {
            // Export the remote object
            BookingRemoteService stub = (BookingRemoteService) UnicastRemoteObject.exportObject(
                bookingRemoteService, servicePort + 1);
            
            // Track exported object
            exportedObjects.add(bookingRemoteService);
            
            // Bind to registry
            Registry reg = rmiRegistry();
            try {
                reg.bind("BookingService", stub);
                log.info("BookingRemoteService bound to registry with name: BookingService");
            } catch (AlreadyBoundException e) {
                reg.rebind("BookingService", stub);
                log.info("BookingRemoteService rebound to registry with name: BookingService");
            }
            
            return stub;
        } catch (RemoteException e) {
            log.error("Failed to export BookingRemoteService: {}", e.getMessage());
            throw e;
        }
    }

    private boolean isObjectExported(Object obj) {
        try {
            // Try to get the stub - if it works, object is already exported
            UnicastRemoteObject.toStub((java.rmi.Remote) obj);
            return true;
        } catch (NoSuchObjectException e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T findExistingStub(Object obj) {
        try {
            return (T) UnicastRemoteObject.toStub((java.rmi.Remote) obj);
        } catch (NoSuchObjectException e) {
            log.error("Object not exported but expected to be: {}", e.getMessage());
            return null;
        }
    }

    @PreDestroy
    public void cleanup() {
        log.info("Cleaning up RMI exports...");
        
        for (Object obj : exportedObjects) {
            try {
                UnicastRemoteObject.unexportObject((java.rmi.Remote) obj, true);
                log.info("Successfully unexported object: {}", obj.getClass().getSimpleName());
            } catch (NoSuchObjectException e) {
                log.warn("Object was not exported: {}", obj.getClass().getSimpleName());
            } catch (Exception e) {
                log.error("Failed to unexport object {}: {}", 
                    obj.getClass().getSimpleName(), e.getMessage());
            }
        }
        
        exportedObjects.clear();
        log.info("RMI cleanup completed");
    }
}