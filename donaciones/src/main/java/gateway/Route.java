/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gateway;

public class Route {
    private final String prefix;        // p.ej. /api/donaciones
    private final String targetBaseUrl; // p.ej. http://localhost:8082
    private final boolean requireAuth;

    public Route(String prefix, String targetBaseUrl, boolean requireAuth) {
        this.prefix = prefix;
        this.targetBaseUrl = targetBaseUrl;
        this.requireAuth = requireAuth;
    }

    public String getPrefix() { return prefix; }
    public String getTargetBaseUrl() { return targetBaseUrl; }
    public boolean isRequireAuth() { return requireAuth; }
}
