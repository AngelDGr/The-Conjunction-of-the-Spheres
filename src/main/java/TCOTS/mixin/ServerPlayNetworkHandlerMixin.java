package TCOTS.mixin;

import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.potions.screen.AlchemyTableScreenHandler;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.c2s.common.ClientOptionsC2SPacket;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
//        extends ServerCommonNetworkHandler implements ServerPlayPacketListener, PlayerAssociatedNetworkHandler, TickablePacketListener {
//
//    @Shadow
//    public ServerPlayerEntity player;
//
//
//    public ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
//        super(server, connection, clientData);
//    }
//
//    @Inject(method = "onCraftRequest", at = @At("HEAD"), cancellable = true)
//    private void manageAlchemyTableRecipes(CraftRequestC2SPacket packet, CallbackInfo ci){
//        if(player.currentScreenHandler instanceof AlchemyTableScreenHandler){
//            ServerPlayNetworkHandler thisObject = (ServerPlayNetworkHandler)(Object)this;
//            NetworkThreadUtils.forceMainThread(packet, thisObject, this.player.getServerWorld());
//            this.player.updateLastActionTime();
//            if (this.player.isSpectator() || this.player.currentScreenHandler.syncId != packet.getSyncId() || !(this.player.currentScreenHandler instanceof AlchemyTableScreenHandler)) {
//                return;
//            }
//
//            this.server.getRecipeManager().get(packet.getRecipe()).ifPresent(recipe -> ((AlchemyTableScreenHandler)this.player.currentScreenHandler).Craft(packet.shouldCraftAll(), (AlchemyTableRecipe)recipe, this.player));
//
//            ci.cancel();
//        }
//    }
//
//    @Override
//    public void onHandSwing(HandSwingC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onChatMessage(ChatMessageC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onCommandExecution(CommandExecutionC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onMessageAcknowledgment(MessageAcknowledgmentC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onClientStatus(ClientStatusC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onButtonClick(ButtonClickC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onClickSlot(ClickSlotC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onCraftRequest(CraftRequestC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onCloseHandledScreen(CloseHandledScreenC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onPlayerInteractEntity(PlayerInteractEntityC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onPlayerMove(PlayerMoveC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onUpdatePlayerAbilities(UpdatePlayerAbilitiesC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onPlayerAction(PlayerActionC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onClientCommand(ClientCommandC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onPlayerInput(PlayerInputC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onUpdateSelectedSlot(UpdateSelectedSlotC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onCreativeInventoryAction(CreativeInventoryActionC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onUpdateSign(UpdateSignC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onPlayerInteractBlock(PlayerInteractBlockC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onPlayerInteractItem(PlayerInteractItemC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onSpectatorTeleport(SpectatorTeleportC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onBoatPaddleState(BoatPaddleStateC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onVehicleMove(VehicleMoveC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onTeleportConfirm(TeleportConfirmC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onRecipeBookData(RecipeBookDataC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onRecipeCategoryOptions(RecipeCategoryOptionsC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onAdvancementTab(AdvancementTabC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onRequestCommandCompletions(RequestCommandCompletionsC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onUpdateCommandBlock(UpdateCommandBlockC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onUpdateCommandBlockMinecart(UpdateCommandBlockMinecartC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onPickFromInventory(PickFromInventoryC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onRenameItem(RenameItemC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onUpdateBeacon(UpdateBeaconC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onUpdateStructureBlock(UpdateStructureBlockC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onSelectMerchantTrade(SelectMerchantTradeC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onBookUpdate(BookUpdateC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onQueryEntityNbt(QueryEntityNbtC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onSlotChangedState(SlotChangedStateC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onQueryBlockNbt(QueryBlockNbtC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onUpdateJigsaw(UpdateJigsawC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onJigsawGenerating(JigsawGeneratingC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onUpdateDifficulty(UpdateDifficultyC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onUpdateDifficultyLock(UpdateDifficultyLockC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onPlayerSession(PlayerSessionC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onAcknowledgeReconfiguration(AcknowledgeReconfigurationC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onAcknowledgeChunks(AcknowledgeChunksC2SPacket packet) {
//
//    }
//
//    @Override
//    public GameProfile getProfile() {
//        return null;
//    }
//
//    @Override
//    public void onClientOptions(ClientOptionsC2SPacket packet) {
//
//    }
//
//    @Override
//    public void onQueryPing(QueryPingC2SPacket packet) {
//
//    }
//
//    @Override
//    public void tick() {
//
//    }
//
//    @Override
//    public boolean isConnectionOpen() {
//        return false;
//    }
//
//    @Override
//    public ServerPlayerEntity getPlayer() {
//        return null;
//    }
}
