package com.app.springboot.demo.controllers;

import com.app.springboot.demo.entity.Rol;
import com.app.springboot.demo.entity.Usuario;
import com.app.springboot.demo.enums.RolNombre;
import com.app.springboot.demo.service.RolService;
import com.app.springboot.demo.service.UsuarioService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String registro(){
        return "registro";
    }

    @PostMapping("/registrar")
    public ModelAndView registrar(String nombreUsuario, String password){
        ModelAndView mv = new ModelAndView();
        if(StringUtils.isBlank(nombreUsuario)){
            mv.setViewName("/registro");
            mv.addObject("error", "el nombre no puede estar vacío");
            return mv;
        }
        if(StringUtils.isBlank(password)){
            mv.setViewName("/registro");
            mv.addObject("error", "la contraseña no puede estar vacía");
            return mv;
        }
        if(usuarioService.existsByNombreusuario(nombreUsuario)){
            mv.setViewName("/registro");
            mv.addObject("error", "ese nombre de usuario ya existe");
            return mv;
        }
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setPassword(passwordEncoder.encode(password));
        Rol rolUser = rolService.getByRolNombre(RolNombre.ROLE_USER).get();
        Set<Rol> roles = new HashSet<>();
        roles.add(rolUser);
        usuario.setRoles(roles);
        usuarioService.save(usuario);
        mv.setViewName("/login");
        mv.addObject("registroOK", "Cuenta creada, " + usuario.getNombreUsuario() + ", ya puedes iniciar sesión");
        return mv;
    }



}
