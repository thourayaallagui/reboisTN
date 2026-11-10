# | Méthode | URI | Description | Codes |

# |---|---|---|---|

# | GET | /api/utilisateurs/hello | Vérifier que le service répond | 200 |

# | GET | /api/utilisateurs | Lister les utilisateurs | 200 |

# | GET | /api/utilisateurs/{id} | Détail d'un utilisateur | 200, 404 |

# | GET | /api/utilisateurs/me | Profil de l'utilisateur connecté | 200, 401, 404 |

# | POST | /api/utilisateurs | Créer un utilisateur | 201, 400, 409 |

# | PUT | /api/utilisateurs/{id} | Modifier un utilisateur | 200, 400, 404 |

# | DELETE | /api/utilisateurs/{id} | Supprimer un utilisateur | 204, 404 |

# | GET | /api/utilisateurs/organisations | Lister les organisations | 200 |

# | GET | /api/utilisateurs/organisations/{id} | Détail d'une organisation | 200, 404 |

# | POST | /api/utilisateurs/organisations | Créer une organisation | 201, 400 |

